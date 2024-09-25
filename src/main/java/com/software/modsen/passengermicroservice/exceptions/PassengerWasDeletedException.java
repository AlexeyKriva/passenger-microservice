package com.software.modsen.passengermicroservice.exceptions;

public class PassengerWasDeletedException extends RuntimeException {
    public PassengerWasDeletedException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}