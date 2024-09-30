package com.software.modsen.passengermicroservice.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<String> passengerNotFoundExceptionHandler(PassengerNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException
                                                                                        exception) {
        return new ResponseEntity<>(exception.getMethod() + METHOD_NOT_ALLOWED_MESSAGE, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) {
        return new ResponseEntity<>(String.format(INVALID_TYPE_FOR_PARAMETER_MESSAGE, exception.getName(),
                exception.getRequiredType().getSimpleName(), exception.getValue()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PassengerWasDeletedException.class)
    public ResponseEntity<String> passengerWasDeletedExceptionHandler(PassengerWasDeletedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException
                                                                         exception) {
        return new ResponseEntity<>(DATA_INTEGRITY_VIOLATION_MESSAGE, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> noHandlerFoundExceptionHandler(NoHandlerFoundException exception) {
        return new ResponseEntity<>(REQUEST_RESOURCE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> httpMessageNotReadableExceptionMessageHandler(HttpMessageNotReadableException
                                                                         exception) {
        return new ResponseEntity<>(INVALID_JSON_FORMAT, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PassengerRatingNotFoundException.class)
    public ResponseEntity<String> passengerRatingNotFoundExceptionHandler(PassengerRatingNotFoundException
                                                                          exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ListenerExecutionFailedException.class)
    public ResponseEntity<String> listenerExecutionFailedExceptionHandler(ListenerExecutionFailedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PassengerAccountNotFoundException.class)
    public ResponseEntity<String> passengerAccountNotFoundExceptionHandler(
            PassengerAccountNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientAccountBalanceException.class)
    public ResponseEntity<String> insufficientAccountBalanceExceptionHandler(
            InsufficientAccountBalanceException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}