package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingMessage;

import java.util.ArrayList;
import java.util.List;

public class PassengerSubject {
    private List<PassengerObserver> passengerObservers = new ArrayList<>();

    public void addPassengerObserver(PassengerObserver passengerObserver) {
        passengerObservers.add(passengerObserver);
    }

    public void removePassengerObserver(PassengerObserver passengerObserver) {
        passengerObservers.remove(passengerObserver);
    }

    public void notifyPassengerObservers(PassengerRatingMessage passengerRatingDto) {
        for (PassengerObserver passengerObserver: passengerObservers) {
            passengerObserver.savePassengerRating(passengerRatingDto);
        }
    }
}
