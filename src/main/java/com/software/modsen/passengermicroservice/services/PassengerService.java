package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.exceptions.DatabaseConnectionRefusedException;
import com.software.modsen.passengermicroservice.exceptions.ErrorMessage;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.observer.PassengerSubject;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

@Service
@AllArgsConstructor
public class PassengerService {
    private PassengerRepository passengerRepository;
    private PassengerSubject passengerSubject;

    public Flux<Passenger> getAllPassengersOrPassengerByName(boolean includeDeleted, String name) {
        if (name != null) {
            return passengerRepository.findByName(name)
                    .flux();
        } else if (includeDeleted) {
            return passengerRepository.findAll();
        } else {
            return passengerRepository.findAll()
                    .filter(passenger -> !passenger.isDeleted());
        }
    }

    public Mono<Passenger> getPassengerByName(String name) {
        Mono<Passenger> passengerFromDb = passengerRepository.findByName(name);

        return passengerFromDb.switchIfEmpty(
                Mono.error(new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE)));
    }

    public Mono<Passenger> getPassengerById(String id) {
        Mono<Passenger> passengerFromDb = passengerRepository.findById(id);

        return passengerFromDb.switchIfEmpty(
                Mono.error(new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE)));
    }

    public Mono<Passenger> savePassenger(Passenger newPassenger) {
        return passengerRepository.save(newPassenger)
                .doOnSuccess(passenger -> passengerSubject.notifyPassengerObservers(passenger.getId()));
    }

    public Mono<Passenger> updatePassengerById(String id, Passenger updatingPassenger) {
        return passengerRepository.findById(id)
                .flatMap(passenger -> {
                    if (passenger.isDeleted()) {
                        return Mono.error(new PassengerWasDeletedException(ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE));
                    }

                    updatingPassenger.setId(passenger.getId());
                    return passengerRepository.save(updatingPassenger);
                })
                .switchIfEmpty(Mono.error(new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE)));
    }

    public Mono<Passenger> patchPassengerById(String id, Passenger updatingPassenger) {
        return passengerRepository.findById(id)
                .flatMap(passenger -> {
                    if (passenger.isDeleted()) {
                        return Mono.error(new PassengerWasDeletedException(ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE));
                    }

                    if (updatingPassenger.getName() == null) {
                        updatingPassenger.setName(passenger.getName());
                    }
                    if (updatingPassenger.getEmail() == null) {
                        updatingPassenger.setEmail(passenger.getEmail());
                    }
                    if (updatingPassenger.getPhoneNumber() == null) {
                        updatingPassenger.setPhoneNumber(passenger.getPhoneNumber());
                    }

                    updatingPassenger.setDeleted(passenger.isDeleted());
                    updatingPassenger.setId(passenger.getId());

                    return passengerRepository.save(updatingPassenger);
                })
                .switchIfEmpty(Mono.error(new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE)));
    }

    @Transactional
    public Mono<Passenger> softDeletePassengerById(String id) {
        return passengerRepository.findById(id)
                .flatMap(passenger -> {
                    passenger.setDeleted(true);
                    return passengerRepository.save(passenger);
                })
                .switchIfEmpty(Mono.error(new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE)));
    }

    public Mono<Passenger> softRecoveryPassengerById(String id) {
        return passengerRepository.findById(id)
                .flatMap(passenger -> {
                    passenger.setDeleted(false);
                    return passengerRepository.save(passenger);
                })
                .switchIfEmpty(Mono.error(new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE)));
    }

    @Recover
    public Passenger fallbackPostgresHandle(Throwable throwable) {
        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_UPDATE_DATA_MESSAGE);
    }

    @Recover
    public List<Passenger> recoverToPSQLException(Throwable throwable) {
        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_GET_DATA_MESSAGE);
    }
}