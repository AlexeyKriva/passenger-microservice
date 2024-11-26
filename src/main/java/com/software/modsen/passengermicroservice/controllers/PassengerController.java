package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.PassengerPatchDto;
import com.software.modsen.passengermicroservice.mappers.PassengerMapper;
import com.software.modsen.passengermicroservice.services.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/api/passengers", produces = "application/json")
@AllArgsConstructor
@Tag(name = "Passenger controller", description = "Allows to interact with passengers.")
public class PassengerController {
    private PassengerService passengerService;
    private final PassengerMapper PASSENGER_MAPPER = PassengerMapper.INSTANCE;

    @GetMapping
    @Operation(
            description = "Allows to get all passengers."
    )
    public Flux<Passenger> getAllPassengersOrPassengerById(@RequestParam(name = "includeDeleted",
            required = false, defaultValue = "true") boolean includeDeleted, @RequestParam(name = "name",
            required = false) String name) {
        return passengerService.getAllPassengersOrPassengerByName(includeDeleted, name);
    }

    @GetMapping("/{id}")
    @Operation(
            description = "Allows to get not deleted passenger by id."
    )
    public Mono<Passenger> getPassenger(@PathVariable("id") @Parameter(description = "Passenger id.")
                                                      String id) {
        return passengerService.getPassengerById(id);
    }

    @PostMapping
    @Operation(
            description = "Allows to save new passenger."
    )
    public Mono<Passenger> savePassenger(@Valid
                                                   @RequestBody @Parameter(description = "Passenger entity.")
                                                       PassengerDto passengerDto) {
        return passengerService.savePassenger(PASSENGER_MAPPER.fromPassengerDtoToPassenger(passengerDto));
    }

    @PutMapping("/{id}")
    @Operation(
            description = "Allows to update all passenger fields."
    )
    public Mono<Passenger> updatePassenger(@PathVariable("id") @Parameter(description = "Passenger id.")
                                                         String id,
                                                     @Valid @RequestBody @Parameter(description = "Entity passenger.")
                                                     PassengerDto passengerDto) {
        return passengerService.updatePassengerById(id, PASSENGER_MAPPER.fromPassengerDtoToPassenger(passengerDto));
    }

    @PatchMapping("/{id}")
    @Operation(
            description = "Allows you to selectively update passenger fields."
    )
    public Mono<Passenger> patchPassenger(@PathVariable("id") @Parameter(description = "Passenger id.")
                                                        String id,
                                                    @Valid
                                                    @RequestBody @Parameter(description = "Entity passenger.")
                                                    PassengerPatchDto passengerPatchDto) {
        return passengerService.patchPassengerById(id,
                PASSENGER_MAPPER.fromPassengerPatchDtoToPassengerRating(passengerPatchDto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            description = "Allows you to soft delete passenger by id."
    )
    public Mono<Passenger> softDeletePassenger(@PathVariable("id") @Parameter(description = "Passenger id.")
                                                             String id) {
        return passengerService.softDeletePassengerById(id);
    }

    @PostMapping("/{id}/restore")
    @Operation(
            description = "Allows you to soft recovery passenger by id."
    )
    public Mono<Passenger> softRecoveryPassenger(@PathVariable("id") @Parameter(description = "Passenger id.")
                                                               String id) {
        return passengerService.softRecoveryPassengerById(id);
    }
}