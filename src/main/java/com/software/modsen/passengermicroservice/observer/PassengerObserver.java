package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingDto;

public interface PassengerObserver {
    void savePassengerRating(PassengerRatingDto passengerRatingDto);
}
