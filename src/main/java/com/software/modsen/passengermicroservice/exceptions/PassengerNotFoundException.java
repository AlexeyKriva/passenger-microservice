package com.software.modsen.passengermicroservice.exceptions;

public class PassengerNotFoundException extends RuntimeException {
    public PassengerNotFoundException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}