package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerRatingNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

@Service
@AllArgsConstructor
public class PassengerRatingService {
    private PassengerRatingRepository passengerRatingRepository;
    private PassengerRepository passengerRepository;

    public List<PassengerRating> getAllPassengerRatings() {
        return passengerRatingRepository.findAll();
    }

    public List<PassengerRating> getAllNotDeletedPassengerRatings() {
        List<PassengerRating> passengerRatingsFromDb = passengerRatingRepository.findAll();
        List<PassengerRating> passengerRatingsAndNotDeleted = new ArrayList<>();

        for (PassengerRating passengerRatingFromDb: passengerRatingsFromDb) {
            Optional<Passenger> passengerFromDb = passengerRepository
                    .findPassengerByIdAndIsDeleted(passengerRatingFromDb.getPassenger().getId(), false);

            if (passengerFromDb.isPresent()) {
                passengerRatingsAndNotDeleted.add(passengerRatingFromDb);
            }
        }

        if (passengerRatingsAndNotDeleted.isEmpty()) {
            throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
        }

        return passengerRatingsAndNotDeleted;
    }

    public PassengerRating getPassengerRatingById(long id) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);

        if (passengerRatingFromDb.isPresent()) {
            Optional<Passenger> passengerFromDb = passengerRepository.findPassengerByIdAndIsDeleted(
                    passengerRatingFromDb.get().getPassenger().getId(), false);
            if (passengerFromDb.isPresent()) {
                return passengerRatingFromDb.get();
            }

            throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE);
    }

    public PassengerRating getPassengerRatingByPassengerId(long passengerId) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findByPassengerId(passengerId);

        if (passengerRatingFromDb.isPresent()) {
            return passengerRatingFromDb.get();
        }

        throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
    }

    public PassengerRating getPassengerRatingByIdAndNotDeleted(long passengerId) {
        Optional<Passenger> passengerFromDb = passengerRepository
                .findPassengerByIdAndIsDeleted(passengerId, false);

        if (passengerFromDb.isPresent()) {
            Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findByPassengerId(passengerId);
            return passengerRatingFromDb.get();
        }

        throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public PassengerRating putPassengerRatingById(long id, Long passengerId, PassengerRating updatingPassengerRating) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);

        if (passengerRatingFromDb.isPresent()) {
            updatingPassengerRating.setId(id);

            Optional<Passenger> passengerFromDb = passengerRepository.findById(passengerId);

            if (passengerFromDb.isPresent()) {
                if (!passengerFromDb.get().isDeleted()) {
                    updatingPassengerRating.setPassenger(passengerFromDb.get());
                } else {
                    throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
                }

                return passengerRatingRepository.save(updatingPassengerRating);
            }

            throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
        }

        throw new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE);
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public PassengerRating patchPassengerRatingById(long id, Long passengerId,
                                                    PassengerRating updatingPassengerRating) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);

        if (passengerRatingFromDb.isPresent()) {
            Optional<Passenger> passengerFromDb;

            if (passengerId == null) {
                passengerFromDb = passengerRepository.findById(
                        passengerRatingFromDb.get().getPassenger().getId());
            } else {
                passengerFromDb = passengerRepository.findById(passengerId);
                if (passengerFromDb.isEmpty()) {
                    throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
                }
            }

            if (!passengerFromDb.get().isDeleted()) {
                updatingPassengerRating.setPassenger(passengerFromDb.get());
            } else {
                throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
            }

            if (updatingPassengerRating.getRatingValue() == null) {
                updatingPassengerRating.setRatingValue(passengerRatingFromDb.get().getRatingValue());
            }

            if (updatingPassengerRating.getNumberOfRatings() == null) {
                updatingPassengerRating.setNumberOfRatings(passengerRatingFromDb.get().getNumberOfRatings());
            }

            updatingPassengerRating.setId(id);

            return passengerRatingRepository.save(updatingPassengerRating);
        }

        throw new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE);
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public void deletePassengerRatingById(long id) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);

        passengerRatingFromDb.ifPresentOrElse(
                passengerRating -> passengerRatingRepository.deleteById(id),
                () -> {throw new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE);}
        );
    }
}