package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.PassengerPatchDto;
import com.software.modsen.passengermicroservice.services.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/passenger", produces = "application/json")
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable("id") long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    @PostMapping
    public ResponseEntity<Passenger> savePassenger(@Valid
                                                   @RequestBody PassengerDto passengerDto) {
        System.out.println(passengerDto);
        return ResponseEntity.ok(passengerService.savePassenger(passengerDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable("id") long id,
                                                     @Valid
                                                     @RequestBody PassengerDto passengerDto) {
        return ResponseEntity.ok(passengerService.updatePassengerById(id, passengerDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Passenger> patchPassenger(@PathVariable("id") long id,
                                                    @Valid
                                                    @RequestBody PassengerPatchDto passengerPatchDto) {
        return ResponseEntity.ok(passengerService.patchPassengerById(id, passengerPatchDto));
    }

    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<Passenger> softDeletePassenger(@PathVariable("id") long id) {
        return ResponseEntity.ok(passengerService.softDeletePassengerById(id));
    }
}