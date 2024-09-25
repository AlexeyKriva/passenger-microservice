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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
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

    public Passenger savePassenger(PassengerDto passengerDto) {
        Passenger newPassenger = PASSENGER_MAPPER.fromPassengerDtoToPassenger(passengerDto);
        Passenger passengerFromDb = passengerRepository.save(newPassenger);
        passengerSubject.notifyPassengerObservers(new PassengerRatingDto(passengerFromDb.getId(), 0));

        return passengerFromDb;
    }

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

    public Passenger softDeletePassengerById(long id) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(id);
        return passengerFromDb.map(passenger -> {
            passenger.setDeleted(true);
            return passengerRepository.save(passenger);
        }).orElseThrow(() -> new PassengerNotFoundException(ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE));
    }
}