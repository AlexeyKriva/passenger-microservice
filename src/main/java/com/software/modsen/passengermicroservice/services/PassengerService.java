package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.exceptions.DatabaseConnectionRefusedException;
import com.software.modsen.passengermicroservice.exceptions.ErrorMessage;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.observer.PassengerSubject;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

@Service
@AllArgsConstructor
public class PassengerService {
    private PassengerRepository passengerRepository;
    private PassengerSubject passengerSubject;

    @Retryable(retryFor = {PSQLException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    @Retryable(retryFor = {PSQLException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    public Passenger getPassengerById(long id) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        if (passengerFromDb.isPresent()) {
            return passengerFromDb.get();
        }

        throw new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE);
    }

    @Retryable(retryFor = {PSQLException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    public List<Passenger> getNotDeletedAllPassengers() {
        return passengerRepository.findAll().stream()
                .filter(passenger -> !passenger.isDeleted())
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "simpleCircuitBreaker", fallbackMethod = "fallbackPostgresHandle")
    @Transactional
    public Passenger savePassenger(Passenger newPassenger) {
        Passenger passengerFromDb = passengerRepository.save(newPassenger);
        passengerSubject.notifyPassengerObservers(passengerFromDb.getId());

        return passengerFromDb;
    }

    @CircuitBreaker(name = "simpleCircuitBreaker", fallbackMethod = "fallbackPostgresHandle")
    @Transactional
    public Passenger updatePassengerById(long id, Passenger updatingPassenger) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        if (passengerFromDb.isPresent()) {
            if (!passengerFromDb.get().isDeleted()) {
                updatingPassenger.setId(passengerFromDb.get().getId());

                return passengerRepository.save(updatingPassenger);
            }

            throw new PassengerWasDeletedException(ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE);
    }

    @CircuitBreaker(name = "simpleCircuitBreaker", fallbackMethod = "fallbackPostgresHandle")
    @Transactional
    public Passenger patchPassengerById(long id, Passenger updatingPassenger) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        if (passengerFromDb.isPresent()) {
            if (!passengerFromDb.get().isDeleted()) {
                if (updatingPassenger.getName() == null) {
                    updatingPassenger.setName(passengerFromDb.get().getName());
                }
                if (updatingPassenger.getEmail() == null) {
                    updatingPassenger.setEmail(passengerFromDb.get().getEmail());
                }
                if (updatingPassenger.getPhoneNumber() == null) {
                    updatingPassenger.setPhoneNumber(passengerFromDb.get().getPhoneNumber());
                }
                updatingPassenger.setDeleted(passengerFromDb.get().isDeleted());
                updatingPassenger.setId(passengerFromDb.get().getId());

                return passengerRepository.save(updatingPassenger);
            }

            throw new PassengerWasDeletedException(ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE);
    }

    @CircuitBreaker(name = "simpleCircuitBreaker", fallbackMethod = "fallbackPostgresHandle")
    @Transactional
    public Passenger softDeletePassengerById(long id) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        return passengerFromDb.map(passenger -> {
            passenger.setDeleted(true);
            return passengerRepository.save(passenger);
        }).orElseThrow(() -> new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE));
    }

    @CircuitBreaker(name = "simpleCircuitBreaker", fallbackMethod = "fallbackPostgresHandle")
    @Transactional
    public Passenger softRecoveryPassengerById(long id) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        return passengerFromDb.map(passenger -> {
            passenger.setDeleted(false);
            return passengerRepository.save(passenger);
        }).orElseThrow(() -> new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE));
    }

    @Recover
    public Passenger fallbackPostgresHandle(Throwable throwable) {
        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_UPDATE_DATA_MESSAGE);
    }

    @Recover
    public List<Passenger> recoverToPSQLException(Throwable throwable) {
        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_GET_DATA_MESSAGE);
    }
}