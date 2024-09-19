package org.modsen.passengermicroservice.services;

import org.modsen.passengermicroservice.entities.Passenger;
import org.modsen.passengermicroservice.entities.PassengerDto;
import org.modsen.passengermicroservice.entities.PassengerPatchDto;

import java.util.List;

public interface PassengerService {
    Passenger getPassengerById(long id);
    List<Passenger> getAllPassengers();
    Passenger savePassenger(PassengerDto passengerDto);
    Passenger updatePassengerById(long id, PassengerDto passengerDto);
    Passenger patchPassengerById(long id, PassengerPatchDto passengerPatchDto);
    Passenger softDeletePassengerById(long id);
}