package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.PassengerPatchDto;
import com.software.modsen.passengermicroservice.entities.account.Currency;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingMessage;
import com.software.modsen.passengermicroservice.exceptions.ErrorMessage;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.observer.PassengerSubject;
import com.software.modsen.passengermicroservice.repositories.PassengerAccountRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private PassengerAccountRepository passengerAccountRepository;
    private PassengerSubject passengerSubject;

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Passenger getPassengerById(long id) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        if (passengerFromDb.isPresent()) {
            if (!passengerFromDb.get().isDeleted()) {
                return passengerFromDb.get();
            }

            throw new PassengerWasDeletedException(ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE);
    }

    public List<Passenger> getNotDeletedAllPassengers() {
        return passengerRepository.findAll().stream()
                .filter(passenger -> !passenger.isDeleted())
                .collect(Collectors.toList());
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public Passenger savePassenger(Passenger newPassenger) {
        Passenger passengerFromDb = passengerRepository.save(newPassenger);
        passengerSubject.notifyPassengerObservers(passengerFromDb.getId());

        return passengerFromDb;
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
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

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
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

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public Passenger softDeletePassengerById(long id) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        return passengerFromDb.map(passenger -> {
            passenger.setDeleted(true);
            return passengerRepository.save(passenger);
        }).orElseThrow(() -> new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE));
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public Passenger softRecoveryPassengerById(long id) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        return passengerFromDb.map(passenger -> {
            passenger.setDeleted(false);
            return passengerRepository.save(passenger);
        }).orElseThrow(() -> new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE));
    }
}