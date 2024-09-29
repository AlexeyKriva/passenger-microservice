package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingMessage;

public interface PassengerObserver {
    void savePassengerRating(PassengerRatingMessage passengerRatingDto);
}
