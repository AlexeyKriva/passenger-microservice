package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPatchDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPutDto;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerRatingNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.mappers.PassengerRatingMapper;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

@Service
@AllArgsConstructor
public class PassengerRatingService {
    private PassengerRatingRepository passengerRatingRepository;
    private PassengerRepository passengerRepository;
    private final PassengerRatingMapper PASSENGER_RATING_MAPPER = PassengerRatingMapper.INSTANCE;

    public List<PassengerRating> getAllPassengerRatings() {
        return passengerRatingRepository.findAll();
    }

    public List<PassengerRating> getAllPassengerRatingsAndNotDeleted() {
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


    public PassengerRating getPassengerRatingById(long passengerId) {
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

    public PassengerRating updatePassengerRating(PassengerRatingDto passengerRatingDto) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(passengerRatingDto.getPassengerId());

        if (passengerFromDb.isPresent()) {
            if (!passengerFromDb.get().isDeleted()) {
                Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository
                        .findByPassengerId(passengerRatingDto.getPassengerId());

                if (passengerRatingFromDb.isPresent()) {
                    PassengerRating updatingPassengerRating = passengerRatingFromDb.get();
                    PASSENGER_RATING_MAPPER
                            .updatePassengerRatingFromPassengerRatingDto(passengerRatingDto, updatingPassengerRating);

                    return passengerRatingRepository.save(updatingPassengerRating);
                }
            }

            throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
    }

    public PassengerRating putPassengerRatingById(long id, PassengerRatingPutDto passengerRatingPutDto) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);
        if (passengerRatingFromDb.isPresent()) {
            PassengerRating updatingPassengerRating = PASSENGER_RATING_MAPPER
                    .fromPassengerRatingPutDtoToPassengerRating(passengerRatingPutDto);
            updatingPassengerRating.setId(id);

            Optional<Passenger> passengerFromDb = passengerRepository.findById(passengerRatingFromDb.get().getId());
            if (!passengerFromDb.get().isDeleted()) {
                updatingPassengerRating.setPassenger(passengerFromDb.get());
            } else {
                throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
            }

            return passengerRatingRepository.save(updatingPassengerRating);
        }

        throw new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE);
    }

    public PassengerRating patchPassengerRatingById(long id, PassengerRatingPatchDto passengerRatingPatchDto) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);
        if (passengerRatingFromDb.isPresent()) {
            PassengerRating updatingPassengerRating = passengerRatingFromDb.get();
            PASSENGER_RATING_MAPPER.updatePassengerRatingFromPassengerRatingPatchDto(passengerRatingPatchDto,
                    updatingPassengerRating);
            if (passengerRatingPatchDto.getPassengerId() != null) {
                Optional<Passenger> passengerFromDb = passengerRepository
                        .findById(passengerRatingPatchDto.getPassengerId());
                if (passengerFromDb.isPresent()) {
                    if (!passengerFromDb.get().isDeleted()) {
                        updatingPassengerRating.setPassenger(passengerFromDb.get());
                    } else {
                        throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
                    }
                } else {
                    throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
                }
            }

            return passengerRatingRepository.save(updatingPassengerRating);
        }

        throw new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE);
    }

    public void deletePassengerRatingById(long id) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);

        passengerRatingFromDb.ifPresentOrElse(
                passengerRating -> passengerRatingRepository.deleteById(id),
                () -> {throw new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE);}
        );
    }
}