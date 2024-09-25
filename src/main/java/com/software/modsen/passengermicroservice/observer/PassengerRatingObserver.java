package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingDto;
import com.software.modsen.passengermicroservice.mappers.PassengerRatingMapper;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PassengerRatingObserver implements PassengerObserver{
    private PassengerRatingRepository passengerRatingRepository;
    private final PassengerRatingMapper PASSENGER_RATING_MAPPER = PassengerRatingMapper.INSTANCE;

    @Override
    public void savePassengerRating(PassengerRatingDto passengerRatingDto) {
        PassengerRating newPassengerRating = PASSENGER_RATING_MAPPER
                .fromPassengerRatingDtoToPassengerRating(passengerRatingDto);
        passengerRatingRepository.save(newPassengerRating);
    }
}
