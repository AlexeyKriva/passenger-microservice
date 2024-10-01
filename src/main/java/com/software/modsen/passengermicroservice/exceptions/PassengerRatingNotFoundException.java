package com.software.modsen.passengermicroservice.exceptions;

public class PassengerRatingNotFoundException extends RuntimeException {
    public PassengerRatingNotFoundException(String message) {
        super(message);
    }
}