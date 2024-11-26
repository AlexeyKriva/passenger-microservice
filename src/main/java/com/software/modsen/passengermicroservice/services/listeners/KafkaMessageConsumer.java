package com.software.modsen.passengermicroservice.services.listeners;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingMessage;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerRatingNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.mappers.PassengerRatingMapper;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE;
import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE;

@Component
@AllArgsConstructor
public class KafkaMessageConsumer {
    private PassengerRepository passengerRepository;
    private PassengerRatingRepository passengerRatingRepository;
    private final PassengerRatingMapper PASSENGER_RATING_MAPPER = PassengerRatingMapper.INSTANCE;

    @KafkaListener(topics = "passenger-create-rating-topic", groupId = "passenger-ratings")
    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    public Mono<PassengerRating> updatePassengerRating(PassengerRatingMessage passengerRatingMessage) {
        return passengerRepository.findById(passengerRatingMessage.getPassengerId())
                .flatMap(passenger -> {
                    if (passenger.isDeleted()) {
                        return Mono.error(new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE));
                    }

                    return passengerRatingRepository.findByPassengerId(passengerRatingMessage.getPassengerId())
                            .flatMap(passengerRatingFromDb -> {
                                // Обновляем рейтинг
                                PASSENGER_RATING_MAPPER.updatePassengerRatingFromPassengerRatingDto(
                                        passengerRatingMessage, passengerRatingFromDb);

                                // Сохраняем обновлённый рейтинг
                                return passengerRatingRepository.save(passengerRatingFromDb);
                            })
                            .switchIfEmpty(Mono.error(new PassengerRatingNotFoundException(
                                    "Passenger rating not found")));
                })
                .switchIfEmpty(Mono.error(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE)));
    }
}