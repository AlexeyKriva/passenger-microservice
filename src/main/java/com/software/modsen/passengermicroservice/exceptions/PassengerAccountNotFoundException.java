package com.software.modsen.passengermicroservice.exceptions;

public class PassengerAccountNotFoundException extends RuntimeException {
    public PassengerAccountNotFoundException(String message) {
        super(message);
    }
}
