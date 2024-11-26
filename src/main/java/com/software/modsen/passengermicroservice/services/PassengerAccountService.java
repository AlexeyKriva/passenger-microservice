package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.exceptions.*;
import com.software.modsen.passengermicroservice.repositories.PassengerAccountRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PassengerAccountService {
    private PassengerAccountRepository passengerAccountRepository;
    private PassengerRepository passengerRepository;

    public Flux<PassengerAccount> getAllPassengerAccounts(boolean includeDeleted) {
        if (includeDeleted) {
            return passengerAccountRepository.findAll();
        } else {
            return passengerAccountRepository.findAll()
                    .flatMap(passengerAccount ->
                            passengerRepository.existsByIdAndDeleted(passengerAccount.getPassengerId(),
                                            false)
                                    .filter(exists -> exists)
                                    .flatMap(exists -> Mono.just(passengerAccount))
                    );
        }
    }

    public Mono<PassengerAccount> getPassengerAccountById(String id) {
        return passengerAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new PassengerAccountNotFoundException(PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE)));
    }

    public Mono<PassengerAccount> getPassengerAccountByPassengerId(String passengerId) {
        return passengerAccountRepository.findByPassengerId(passengerId)
                .flatMap(passengerAccount ->
                        passengerRepository.findById(passengerId)
                                .flatMap(passengerFromDb -> {
                                    if (passengerFromDb.isDeleted()) {
                                        return Mono.error(new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE));
                                    }
                                    return Mono.just(passengerAccount);
                                })
                )
                .switchIfEmpty(Mono.error(new PassengerAccountNotFoundException(PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE)));
    }

    public Mono<PassengerAccount> increaseBalance(String passengerId, PassengerAccount updatingPassengerAccount) {
        return passengerAccountRepository.findByPassengerId(passengerId)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(passengerAccountFromDb -> {
                    Passenger passengerFromDb = passengerRepository.findById(passengerId).block();

                    if (passengerFromDb.isDeleted()) {
                        return Mono.error(new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE));
                    }

                    updatingPassengerAccount.setId(passengerAccountFromDb.getId());
                    updatingPassengerAccount.setVersion(passengerAccountFromDb.getVersion());

                    updatingPassengerAccount.setPassengerId(passengerFromDb.getId());

                    Float increasingBalance = updatingPassengerAccount.getBalance() + passengerAccountFromDb.getBalance();
                    updatingPassengerAccount.setBalance(increasingBalance);

                    return passengerAccountRepository.save(updatingPassengerAccount);
                })
                .switchIfEmpty(Mono.error(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE)));
    }

    public Mono<PassengerAccount> cancelBalance(String passengerId, PassengerAccount updatingPassengerAccount) {
        return passengerAccountRepository.findByPassengerId(passengerId)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(passengerAccountFromDb -> {
                    Mono<Passenger> passengerMono = passengerRepository.findById(passengerId);

                    return passengerMono.flatMap(passengerFromDb -> {
                        if (passengerFromDb.isDeleted()) {
                            return Mono.error(new PassengerWasDeletedException(PASSENGER_WAS_DELETED_MESSAGE));
                        }

                        // Обновляем поля учетной записи
                        updatingPassengerAccount.setId(passengerAccountFromDb.getId());
                        updatingPassengerAccount.setVersion(passengerAccountFromDb.getVersion());
                        updatingPassengerAccount.setPassengerId(passengerFromDb.getId());

                        float resultingBalance = passengerAccountFromDb.getBalance() - updatingPassengerAccount.getBalance();
                        if (resultingBalance >= 0) {
                            updatingPassengerAccount.setBalance(resultingBalance);
                            return passengerAccountRepository.save(updatingPassengerAccount);
                        } else {
                            return Mono.error(new InsufficientAccountBalanceException(INSUFFICIENT_ACCOUNT_BALANCE_EXCEPTION));
                        }
                    });
                })
                .switchIfEmpty(Mono.error(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE)));
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