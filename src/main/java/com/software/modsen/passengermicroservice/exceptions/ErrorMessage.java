package com.software.modsen.passengermicroservice.exceptions;

public class ErrorMessage {
    public static final String PASSENGER_NOT_FOUND_MESSAGE = "Passenger not found.";
    public static final String PASSENGER_WAS_DELETED_MESSAGE = "Passenger was deleted.";
    public static final String PASSENGER_RATING_NOT_FOUND_MESSAGE = "Passenger rating not found.";

    public static final String METHOD_NOT_ALLOWED_MESSAGE = " method is not supported.";
    public static final String INVALID_TYPE_FOR_PARAMETER_MESSAGE = "Invalid value for parameter '%s'. Expected type:" +
            " %s, but got: %s.";
    public static final String DATA_INTEGRITY_VIOLATION_MESSAGE = "This email or phone number has already been" +
            " registered or data in request has null values.";
    public static final String REQUEST_RESOURCE_NOT_FOUND_MESSAGE = "The requested resource was not found. Please" +
            " check the URL and try again.";
    public static final String INVALID_JSON_FORMAT = "Invalid json format.";

    public static final String PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE = "Passenger account not found.";

    public static final String INSUFFICIENT_ACCOUNT_BALANCE_EXCEPTION = "Insufficient account balance.";

    public static final String BAD_CONNECTION_TO_DATABASE_MESSAGE = "Unsuccessful attempt to connect to the database. " +
            "Please, wait and try again later.";

    public static final String CANNOT_GET_DATA_MESSAGE = " For this reason you cannot get the data.";
    public static final String CANNOT_UPDATE_DATA_MESSAGE = " For this reason you cannot update the data.";
    public static final String OPTIMISTIC_LOCK_MESSAGE = "The entity you attempted to modify was modified by another" +
            " transaction. Please reload the entity and try again.";
}