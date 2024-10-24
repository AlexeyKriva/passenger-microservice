package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPatchDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPutDto;
import com.software.modsen.passengermicroservice.mappers.PassengerRatingMapper;
import com.software.modsen.passengermicroservice.services.PassengerRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/passengers", produces = "application/json")
@AllArgsConstructor
@Tag(name = "Passenger rating controller", description = "Allows to interact with passenger ratings.")
public class PassengerRatingController {
    private PassengerRatingService passengerRatingService;
    private final PassengerRatingMapper PASSENGER_RATING_MAPPER = PassengerRatingMapper.INSTANCE;

    @GetMapping("/ratings")
    @Operation(
            description = "Allows to get all passenger ratings."
    )
    public ResponseEntity<List<PassengerRating>> getAllPassengerRatings(@RequestParam(name = "includeDeleted",
            required = false, defaultValue = "true")
                                                                            boolean includeDeleted) {
        return ResponseEntity.ok(passengerRatingService.getAllPassengerRatings(includeDeleted));
    }

    @GetMapping("/ratings/{id}")
    @Operation(
            description = "Allows to get passenger rating by id."
    )
    public ResponseEntity<PassengerRating> getPassengerRatingById(
            @PathVariable("id")
            @Parameter(description = "Passenger rating id.")
            long id) {
        return ResponseEntity.ok(passengerRatingService.getPassengerRatingById(id));
    }

    @GetMapping("/{passenger_id}/ratings")
    @Operation(
            description = "Allows to get passenger rating by passenger id."
    )
    public ResponseEntity<PassengerRating> getPassengerRatingByPassengerId(
            @PathVariable("passenger_id") @Parameter(description = "Passenger id.")
            long id) {
        return ResponseEntity.ok(passengerRatingService.getPassengerRatingByPassengerId(id));
    }

    @PutMapping("/ratings/{id}")
    @Operation(
            description = "Allows to update passenger rating by id."
    )
    public ResponseEntity<PassengerRating> putPassengerRatingById(@PathVariable("id") long id,
                                                                  @Valid @RequestBody
                                                                  @Parameter(description = "Passenger rating entity.")
                                                                  PassengerRatingPutDto passengerRatingPutDto) {
        return ResponseEntity.ok(passengerRatingService.putPassengerRatingById(
                id,
                PASSENGER_RATING_MAPPER.fromPassengerRatingPutDtoToPassengerRating(passengerRatingPutDto)));
    }

    @PatchMapping("/ratings/{id}")
    @Operation(
            description = "Allows to update passenger rating by id."
    )
    public ResponseEntity<PassengerRating> patchPassengerRatingById(@PathVariable("id") long id,
                                                                   @Valid @RequestBody
                                                                   @Parameter(description = "Passenger rating entity.")
                                                                   PassengerRatingPatchDto passengerRatingPatchDto) {
        return ResponseEntity.ok(passengerRatingService.patchPassengerRatingById(
                id,
                PASSENGER_RATING_MAPPER.fromPassengerRatingPatchDtoToPassengerRating(passengerRatingPatchDto)));
    }
}