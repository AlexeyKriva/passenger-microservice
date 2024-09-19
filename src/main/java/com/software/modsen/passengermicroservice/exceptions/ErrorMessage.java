package com.software.modsen.passengermicroservice.exceptions;

public class ErrorMessage {
    public static final String PASSENGER_NOT_FOUND_MESSAGE = "Passenger not found.";
    public static final String PASSENGER_WAS_DELETED_MESSAGE = "Passenger was deleted.";
    public static final String METHOD_NOT_ALLOWED_MESSAGE = " method is not supported.";
    public static final String INVALID_TYPE_FOR_PARAMETER_MESSAGE = "Invalid value for parameter '%s'. Expected type:" +
            " %s, but got: %s.";
}