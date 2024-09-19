package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.PassengerPatchDto;

import java.util.List;

public interface PassengerService {
    Passenger getPassengerById(long id);
    List<Passenger> getAllPassengers();
    Passenger savePassenger(PassengerDto passengerDto);
    Passenger updatePassengerById(long id, PassengerDto passengerDto);
    Passenger patchPassengerById(long id, PassengerPatchDto passengerPatchDto);
    Passenger softDeletePassengerById(long id);
}