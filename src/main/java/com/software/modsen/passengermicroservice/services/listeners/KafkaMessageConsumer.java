package com.software.modsen.passengermicroservice.services.listeners;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingMessage;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.mappers.PassengerRatingMapper;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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
    public PassengerRating updatePassengerRating(PassengerRatingMessage passengerRatingMessage) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(passengerRatingMessage.getPassengerId());

        if (passengerFromDb.isPresent()) {
            if (!passengerFromDb.get().isDeleted()) {
                Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository
                        .findByPassengerId(passengerRatingMessage.getPassengerId());

                if (passengerRatingFromDb.isPresent()) {
                    PassengerRating updatingPassengerRating = passengerRatingFromDb.get();
                    PASSENGER_RATING_MAPPER
                            .updatePassengerRatingFromPassengerRatingDto(passengerRatingMessage, updatingPassengerRating);

                    return passengerRatingRepository.save(updatingPassengerRating);
                }
            }

            throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
    }
}