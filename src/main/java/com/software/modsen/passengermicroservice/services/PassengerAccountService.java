package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.exceptions.*;
import com.software.modsen.passengermicroservice.repositories.PassengerAccountRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PassengerAccountService {
    private PassengerAccountRepository passengerAccountRepository;
    private PassengerRepository passengerRepository;

    @Retryable(retryFor = {PSQLException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    public List<PassengerAccount> getAllPassengerAccounts(boolean includeDeleted) {
        if (includeDeleted) {
            return passengerAccountRepository.findAll();
        } else {
            return passengerAccountRepository.findAll().stream()
                    .filter(passengerAccount -> passengerRepository.existsByIdAndIsDeleted(
                            passengerAccount.getPassenger().getId(), false))
                    .collect(Collectors.toList());
        }
    }

    @Retryable(retryFor = {PSQLException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    public PassengerAccount getPassengerAccountById(long id) {
        Optional<PassengerAccount> passengerAccountFromDb = passengerAccountRepository.findById(id);

        if (passengerAccountFromDb.isPresent()) {
            return passengerAccountFromDb.get();
        }

        throw new PassengerAccountNotFoundException(PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE);
    }

    @Retryable(retryFor = {PSQLException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    public PassengerAccount getPassengerAccountByPassengerId(long passengerId) {
        Optional<PassengerAccount> passengerAccountFromDb = passengerAccountRepository.findByPassengerId(passengerId);

        if (passengerAccountFromDb.isPresent()) {
            if (!passengerAccountFromDb.get().getPassenger().isDeleted()) {
                return passengerAccountFromDb.get();
            }

            throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerAccountNotFoundException(PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE);
    }

    @Transactional
    public PassengerAccount increaseBalance(long passengerId, PassengerAccount updatingPassengerAccount) {
        Optional<PassengerAccount> passengerAccountFromDb = passengerAccountRepository.findByPassengerId(passengerId);

        if (passengerAccountFromDb.isPresent()) {
            updatingPassengerAccount.setId(passengerAccountFromDb.get().getId());
            updatingPassengerAccount.setVersion(passengerAccountFromDb.get().getVersion());

            if (!passengerAccountFromDb.get().getPassenger().isDeleted()) {
                updatingPassengerAccount.setPassenger(passengerAccountFromDb.get().getPassenger());

                Float increasingBalance = updatingPassengerAccount.getBalance()
                        + passengerAccountFromDb.get().getBalance();
                updatingPassengerAccount.setBalance(increasingBalance);

                return passengerAccountRepository.save(updatingPassengerAccount);
            }

            throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
    }

    @CircuitBreaker(name = "simpleCircuitBreaker", fallbackMethod = "fallbackPostgresHandle")
    @Transactional
    public PassengerAccount cancelBalance(long passengerId, PassengerAccount updatingPassengerAccount) {
        Optional<PassengerAccount> passengerAccountFromDb = passengerAccountRepository.findByPassengerId(passengerId);

        if (passengerAccountFromDb.isPresent()) {
            updatingPassengerAccount.setId(passengerAccountFromDb.get().getId());
            updatingPassengerAccount.setVersion(passengerAccountFromDb.get().getVersion());

            if (!passengerAccountFromDb.get().getPassenger().isDeleted()) {
                updatingPassengerAccount.setPassenger(passengerAccountFromDb.get().getPassenger());
                float increasingBalance = passengerAccountFromDb.get().getBalance()
                        - updatingPassengerAccount.getBalance();
                if (increasingBalance >= 0) {
                    updatingPassengerAccount.setBalance(increasingBalance);

                    return passengerAccountRepository.save(updatingPassengerAccount);
                } else {
                    throw new InsufficientAccountBalanceException(INSUFFICIENT_ACCOUNT_BALANCE_EXCEPTION);
                }
            }

            throw new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
    }

    @Recover
    public PassengerAccount fallbackPostgresHandle(Throwable throwable) {
        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_UPDATE_DATA_MESSAGE);
    }

    @Recover
    public List<PassengerAccount> recoverToPSQLException(Throwable throwable) {
        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_GET_DATA_MESSAGE);
    }
}