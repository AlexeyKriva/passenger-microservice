package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.PassengerPatchDto;
import com.software.modsen.passengermicroservice.mappers.PassengerMapper;
import com.software.modsen.passengermicroservice.services.PassengerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/passenger", produces = "application/json")
@AllArgsConstructor
public class PassengerController {
    private PassengerService passengerService;
    private final PassengerMapper PASSENGER_MAPPER = PassengerMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @GetMapping("/not-deleted")
    public ResponseEntity<List<Passenger>> getAllNotDeletedPassengers() {
        return ResponseEntity.ok(passengerService.getNotDeletedAllPassengers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable("id") long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    @PostMapping
    public ResponseEntity<Passenger> savePassenger(@Valid
                                                   @RequestBody PassengerDto passengerDto) {
        return ResponseEntity.ok(passengerService.savePassenger(
                PASSENGER_MAPPER.fromPassengerDtoToPassenger(passengerDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable("id") long id,
                                                     @Valid
                                                     @RequestBody PassengerDto passengerDto) {
        return ResponseEntity.ok(passengerService.updatePassengerById(id,
                PASSENGER_MAPPER.fromPassengerDtoToPassenger(passengerDto)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Passenger> patchPassenger(@PathVariable("id") long id,
                                                    @Valid
                                                    @RequestBody PassengerPatchDto passengerPatchDto) {
        return ResponseEntity.ok(passengerService.patchPassengerById(id,
                PASSENGER_MAPPER.fromPassengerPatchDtoToPassengerRating(passengerPatchDto)));
    }

    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<Passenger> softDeletePassenger(@PathVariable("id") long id) {
        return ResponseEntity.ok(passengerService.softDeletePassengerById(id));
    }
}