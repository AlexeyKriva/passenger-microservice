package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingMessage;
import com.software.modsen.passengermicroservice.mappers.PassengerRatingMapper;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class PassengerRatingObserver implements PassengerObserver{
    private PassengerRepository passengerRepository;
    private PassengerRatingRepository passengerRatingRepository;
    private final PassengerRatingMapper PASSENGER_RATING_MAPPER = PassengerRatingMapper.INSTANCE;

    @Override
    public void savePassengerRating(PassengerRatingMessage passengerRatingDto) {
        PassengerRating newPassengerRating = PASSENGER_RATING_MAPPER
                .fromPassengerRatingDtoToPassengerRating(passengerRatingDto);
        Optional<Passenger> passengerFromDb = passengerRepository.findById(passengerRatingDto.getPassengerId());
        newPassengerRating.setPassenger(passengerFromDb.get());
        newPassengerRating.setNumberOfRatings(0);

        passengerRatingRepository.save(newPassengerRating);
    }
}
