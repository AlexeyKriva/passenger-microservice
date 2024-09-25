package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.PassengerPatchDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingDto;
import com.software.modsen.passengermicroservice.exceptions.ErrorMessage;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.mappers.PassengerMapper;
import com.software.modsen.passengermicroservice.observer.PassengerSubject;
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
    private PassengerSubject passengerSubject;
    private final PassengerMapper PASSENGER_MAPPER = PassengerMapper.INSTANCE;

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

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll().stream()
                .filter(passenger -> !passenger.isDeleted())
                .collect(Collectors.toList());
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public Passenger savePassenger(PassengerDto passengerDto) {
        Passenger newPassenger = PASSENGER_MAPPER.fromPassengerDtoToPassenger(passengerDto);
        Passenger passengerFromDb = passengerRepository.save(newPassenger);
        passengerSubject.notifyPassengerObservers(new PassengerRatingDto(passengerFromDb.getId(), 0));

        return passengerFromDb;
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public Passenger updatePassengerById(long id, PassengerDto passengerDto) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        if (passengerFromDb.isPresent()) {
            if (!passengerFromDb.get().isDeleted()) {
                Passenger updatingPassenger = PASSENGER_MAPPER.fromPassengerDtoToPassenger(passengerDto);
                updatingPassenger.setId(passengerFromDb.get().getId());

                return passengerRepository.save(updatingPassenger);
            }

            throw new PassengerWasDeletedException(ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE);
        }

        throw new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE);
    }

    @Retryable(retryFor = {DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public Passenger patchPassengerById(long id, PassengerPatchDto passengerPatchDto) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);

        if (passengerFromDb.isPresent()) {
            if (!passengerFromDb.get().isDeleted()) {
                Passenger updatingPassenger = passengerFromDb.get();
                PASSENGER_MAPPER.updatePassengerFromPassengerPatchDto(passengerPatchDto, updatingPassenger);

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

    @Recover
    public ResponseEntity<String> dataAccessExceptionRecoverForSaveAndPut(DataAccessException exception,
                                                                          PassengerDto passengerDto) {
        return new ResponseEntity<>(CANNOT_SAVE_PASSENGER_MESSAGE + passengerDto.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Recover
    public ResponseEntity<String> dataAccessExceptionRecoverForPatch(DataAccessException exception,
                                                                     PassengerPatchDto passengerPatchDto) {
        return new ResponseEntity<>(CANNOT_PATCH_PASSENGER_MESSAGE + passengerPatchDto.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Recover
    public ResponseEntity<String> dataAccessExceptionRecoverForDelete(DataAccessException exception,
                                                                      long id) {
        return new ResponseEntity<>(CANNOT_DELETE_PASSENGER_MESSAGE + id,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}