package com.software.modsen.passengermicroservice.exceptions;

public class DatabaseConnectionRefusedException extends RuntimeException {
    public DatabaseConnectionRefusedException(String message) {
        super(message);
    }
}