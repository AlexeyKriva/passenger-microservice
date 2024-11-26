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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PassengerNotFoundException.class)
    public Mono<ResponseEntity<String>> passengerNotFoundExceptionHandler(PassengerNotFoundException exception) {
        return Mono.just(new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", "Bad Request");
        errorDetails.put("message", "Validation failed for argument");

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        errorDetails.put("errors", fieldErrors);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<Map<String, String>>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return Mono.just(new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public Mono<ResponseEntity<String>> httpRequestMethodNotSupportedExceptionHandler(
            MethodNotAllowedException
                    exception) {
        return Mono.just(new ResponseEntity<>(METHOD_NOT_ALLOWED_MESSAGE,
                HttpStatus.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Mono<ResponseEntity<String>> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException
                                                                                               exception) {
        return Mono.just(new ResponseEntity<>(String.format(INVALID_TYPE_FOR_PARAMETER_MESSAGE, exception.getName(),
                exception.getRequiredType().getSimpleName(), exception.getValue()), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(PassengerWasDeletedException.class)
    public Mono<ResponseEntity<String>> passengerWasDeletedExceptionHandler(PassengerWasDeletedException exception) {
        return Mono.just(new ResponseEntity<>(exception.getMessage(), HttpStatus.GONE));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ResponseEntity<String>> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException
                                                                                       exception) {
        return Mono.just(new ResponseEntity<>(DATA_INTEGRITY_VIOLATION_MESSAGE, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Mono<ResponseEntity<String>> httpMessageNotReadableExceptionMessageHandler(HttpMessageNotReadableException
                                                                                              exception) {
        return Mono.just(new ResponseEntity<>(INVALID_JSON_FORMAT, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(PassengerRatingNotFoundException.class)
    public Mono<ResponseEntity<String>> passengerRatingNotFoundExceptionHandler(PassengerRatingNotFoundException
                                                                                        exception) {
        return Mono.just(new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ListenerExecutionFailedException.class)
    public Mono<ResponseEntity<String>> listenerExecutionFailedExceptionHandler(ListenerExecutionFailedException
                                                                                            exception) {
        return Mono.just(new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Mono<ResponseEntity<Object>> handleNoSuchElementException(NoSuchElementException exception) {
        return Mono.just(new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(PassengerAccountNotFoundException.class)
    public Mono<ResponseEntity<String>> passengerAccountNotFoundExceptionHandler(
            PassengerAccountNotFoundException exception) {
        return Mono.just(new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(InsufficientAccountBalanceException.class)
    public Mono<ResponseEntity<String>> insufficientAccountBalanceExceptionHandler(
            InsufficientAccountBalanceException exception) {
        return Mono.just(new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(DatabaseConnectionRefusedException.class)
    public Mono<ResponseEntity<String>> pSQLExceptionHandler(DatabaseConnectionRefusedException exception) {
        return Mono.just(new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}