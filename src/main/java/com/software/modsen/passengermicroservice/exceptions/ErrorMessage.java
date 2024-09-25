package com.software.modsen.passengermicroservice.exceptions;

public class ErrorMessage {
    public static final String PASSENGER_NOT_FOUND_MESSAGE = "Passenger not found.";
    public static final String PASSENGER_WAS_DELETED_MESSAGE = "Passenger was deleted.";
    public static final String PASSENGER_RATING_NOT_FOUND_MESSAGE = "Passenger rating not found.";

    public static final String METHOD_NOT_ALLOWED_MESSAGE = " method is not supported.";
    public static final String INVALID_TYPE_FOR_PARAMETER_MESSAGE = "Invalid value for parameter '%s'. Expected type:" +
            " %s, but got: %s.";
    public static final String DATA_INTEGRITY_VIOLATION_MESSAGE = "This email or phone number has already been" +
            " registered.";
    public static final String REQUEST_RESOURCE_NOT_FOUND_MESSAGE = "The requested resource was not found. Please" +
            " check the URL and try again.";
    public static final String INVALID_JSON_FORMAT = "Invalid json format.";

    public static final String CANNOT_SAVE_PASSENGER_MESSAGE = "Unsuccessful attempt to save passenger ";
    public static final String CANNOT_PATCH_PASSENGER_MESSAGE = "Unsuccessful attempt to patch passenger ";
    public static final String CANNOT_DELETE_PASSENGER_MESSAGE = "Unsuccessful attempt to delete passenger with id ";
    public static final String CANNOT_UPDATE_PASSENGER_RATING_MESSAGE = "Unsuccessful attempt to update passenger" +
            " rating ";
    public static final String CANNOT_PUT_PASSENGER_RATING_MESSAGE = "Unsuccessful attempt to put passenger rating ";
    public static final String CANNOT_PATCH_PASSENGER_RATING_MESSAGE = "Unsuccessful attempt to patch passenger " +
            "rating ";
    public static final String CANNOT_DELETE_PASSENGER_RATING_MESSAGE = "Unsuccessful attempt to delete passenger" +
            "rating with id ";
}