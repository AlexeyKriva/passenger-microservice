package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.exceptions.DatabaseConnectionRefusedException;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerRatingNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

@Service
@AllArgsConstructor
public class PassengerRatingService {
    private PassengerRatingRepository passengerRatingRepository;
    private PassengerRepository passengerRepository;

    public Flux<PassengerRating> getAllPassengerRatings(boolean includeDeleted) {
        if (includeDeleted) {
            return passengerRatingRepository.findAll();
        } else {
            return passengerRatingRepository.findAll()
                    .flatMap(passengerAccount ->
                            passengerRepository.existsByIdAndDeleted(passengerAccount.getPassengerId(),
                                            false)
                                    .filter(exists -> exists)
                                    .flatMap(exists -> Mono.just(passengerAccount))
                    );
        }
    }

    public Mono<PassengerRating> getPassengerRatingById(String id) {
        return passengerRatingRepository.findById(id)
                .switchIfEmpty(Mono.error(new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE)));
    }

    public Mono<PassengerRating> getPassengerRatingByPassengerId(String passengerId) {
        return passengerRatingRepository.findByPassengerId(passengerId)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(passengerRatingFromDb -> {
                    Passenger passengerFromDb = passengerRepository.findById(passengerId).block();

                    if (passengerFromDb.isDeleted()) {
                        return Mono.error(new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE));
                    }

                    return Mono.just(passengerRatingFromDb);
                })
                .switchIfEmpty(Mono.error(new PassengerNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE)));
    }

    @CircuitBreaker(name = "simpleCircuitBreaker", fallbackMethod = "fallbackPostgresHandle")
    @Transactional
    public Mono<PassengerRating> putPassengerRatingById(String id, PassengerRating updatingPassengerRating) {
        return passengerRatingRepository.findById(id)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(passengerRatingFromDb -> {
                    Passenger passengerFromDb = passengerRepository.findById(passengerRatingFromDb.getPassengerId())
                            .block();

                    if (passengerFromDb.isDeleted()) {
                        return Mono.error(new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE));
                    }

                    updatingPassengerRating.setId(id);
                    updatingPassengerRating.setPassengerId(passengerFromDb.getId());
                    return passengerRatingRepository.save(updatingPassengerRating);
                })
                .switchIfEmpty(Mono.error(new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE)));
    }

    public Mono<PassengerRating> patchPassengerRatingById(String id,
                                                    PassengerRating updatingPassengerRating) {
        return passengerRatingRepository.findById(id)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(passengerRatingFromDb -> {
                    Passenger passengerFromDb = passengerRepository.findById(passengerRatingFromDb.getPassengerId())
                            .block();

                    if (passengerFromDb.isDeleted()) {
                        return Mono.error(new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE));
                    }

                    if (updatingPassengerRating.getRatingValue() == null) {
                        updatingPassengerRating.setRatingValue(passengerRatingFromDb.getRatingValue());
                    }

                    if (updatingPassengerRating.getNumberOfRatings() == null) {
                        updatingPassengerRating.setNumberOfRatings(passengerRatingFromDb.getNumberOfRatings());
                    }

                    updatingPassengerRating.setPassengerId(passengerFromDb.getId());
                    updatingPassengerRating.setId(id);

                    return passengerRatingRepository.save(updatingPassengerRating);
                })
                .switchIfEmpty(Mono.error(new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE)));
    }

    @Recover
    public PassengerRating fallbackPostgresHandle(Throwable throwable) {
        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_UPDATE_DATA_MESSAGE);
    }

    @Recover
    public List<PassengerRating> recoverToPSQLException(Throwable throwable) {
        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_GET_DATA_MESSAGE);
    }
}