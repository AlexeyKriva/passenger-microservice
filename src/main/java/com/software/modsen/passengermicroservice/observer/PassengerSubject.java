package com.software.modsen.passengermicroservice.observer;

import org.springframework.transaction.annotation.Transactional;

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

    public void notifyPassengerObservers(String passengerId) {
        for (PassengerObserver passengerObserver: passengerObservers) {
            passengerObserver.updatePassengerInfo(passengerId);
        }
    }
}
