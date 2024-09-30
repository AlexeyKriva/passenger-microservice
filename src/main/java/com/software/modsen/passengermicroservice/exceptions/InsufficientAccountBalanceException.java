package com.software.modsen.passengermicroservice.exceptions;

public class InsufficientAccountBalanceException extends RuntimeException {
    public InsufficientAccountBalanceException(String message) {
        super(message);
    }
}
