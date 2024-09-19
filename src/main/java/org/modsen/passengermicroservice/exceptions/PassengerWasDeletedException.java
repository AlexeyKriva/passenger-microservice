package org.modsen.passengermicroservice.exceptions;

public class PassengerWasDeletedException extends RuntimeException {
    public PassengerWasDeletedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}