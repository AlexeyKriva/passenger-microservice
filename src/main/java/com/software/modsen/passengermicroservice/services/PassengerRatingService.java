package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPatchDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPutDto;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.mappers.PassengerRatingMapper;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE;
import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE;

@Service
@AllArgsConstructor
public class PassengerRatingService {
    private PassengerRatingRepository passengerRatingRepository;
    private PassengerRepository passengerRepository;
    private final PassengerRatingMapper PASSENGER_RATING_MAPPER = PassengerRatingMapper.INSTANCE;

    public List<PassengerRating> getAllPassengerRatings() {
        return passengerRatingRepository.findAll();
    }

    public PassengerRating getPassengerRatingById(long passengerId) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findByPassengerId(passengerId);
        if (passengerRatingFromDb.isPresent()) {
            return passengerRatingFromDb.get();
        }

        throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
    }

    public PassengerRating getPassengerRatingByIdAndNotDeleted(long passengerId) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findByPassengerId(passengerId);
        if (passengerRatingFromDb.isPresent()) {
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

            return passengerRatingRepository.save(updatingPassengerRating);
        }

        throw new RuntimeException();
    }

    public PassengerRating patchPassengerRatingById(long id, PassengerRatingPatchDto passengerRatingPatchDto) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);
        if (passengerRatingFromDb.isPresent()) {
            PassengerRating updatingPassengerRating = passengerRatingFromDb.get();
            PASSENGER_RATING_MAPPER.updatePassengerRatingFromPassengerRatingPatchDto(passengerRatingPatchDto,
                    updatingPassengerRating);

            return passengerRatingRepository.save(updatingPassengerRating);
        }

        throw new RuntimeException();
    }

    public void deletePassengerRatingById(long id) {
        Optional<PassengerRating> passengerRatingFromDb = passengerRatingRepository.findById(id);
        if (passengerRatingFromDb.isPresent()) {
            passengerRatingRepository.deleteById(id);
        }

        throw new RuntimeException();
    }
}