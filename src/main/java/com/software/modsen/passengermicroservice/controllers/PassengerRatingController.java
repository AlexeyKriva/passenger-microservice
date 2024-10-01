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
@RequestMapping(value = "/api/passenger/rating", produces = "application/json")
@AllArgsConstructor
@Tag(name = "Passenger rating controller", description = "Allows to interact with passenger ratings.")
public class PassengerRatingController {
    private PassengerRatingService passengerRatingService;
    private final PassengerRatingMapper PASSENGER_RATING_MAPPER = PassengerRatingMapper.INSTANCE;

    @GetMapping
    @Operation(
            description = "Allows to get all passenger ratings."
    )
    public ResponseEntity<List<PassengerRating>> getAllPassengerRatings() {
        return ResponseEntity.ok(passengerRatingService.getAllPassengerRatings());
    }

    @GetMapping("/not-deleted")
    @Operation(
            description = "Allows to get all not deleted passenger ratings."
    )
    public ResponseEntity<List<PassengerRating>> getAllNotDeletedPassengerRatings() {
        return ResponseEntity.ok(passengerRatingService.getAllNotDeletedPassengerRatings());
    }

    @GetMapping("/{id}")
    @Operation(
            description = "Allows to get passenger rating by id."
    )
    public ResponseEntity<PassengerRating> getPassengerRatingById(
            @PathVariable("id")
            @Parameter(description = "Passenger rating id.")
            long id) {
        return ResponseEntity.ok(passengerRatingService.getPassengerRatingById(id));
    }

    @GetMapping("/{passenger_id}/by-passenger")
    @Operation(
            description = "Allows to get passenger rating by passenger id."
    )
    public ResponseEntity<PassengerRating> getPassengerRatingByPassengerId(
            @PathVariable("passenger_id") @Parameter(description = "Passenger id.")
            long id) {
        return ResponseEntity.ok(passengerRatingService.getPassengerRatingByPassengerId(id));
    }

    @GetMapping("/{passenger_id}/not-deleted")
    @Operation(
            description = "Allows to get not deleted passenger rating by passenger id."
    )
    public ResponseEntity<PassengerRating> getPassengerRatingByPassengerIdAndNotDeleted(
            @PathVariable("passenger_id") @Parameter(description = "Passenger rating id.") long id) {
        return ResponseEntity.ok(passengerRatingService.getPassengerRatingByIdAndNotDeleted(id));
    }

    @PutMapping("/{id}")
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

    @PatchMapping("/{id}")
    @Operation(
            description = "Allows to update passenger rating by id."
    )
    public ResponseEntity<PassengerRating> patchPassengerRatingById(@PathVariable("id") long id,
                                                                   @Valid @RequestBody
                                                                   @Parameter(description = "Passenger rating entity.")
                                                                   PassengerRatingPatchDto passengerRatingPatchDto) {
        return ResponseEntity.ok(passengerRatingService.patchPassengerRatingById(
                id,
                passengerRatingPatchDto.getPassengerId(),
                PASSENGER_RATING_MAPPER.fromPassengerRatingPatchDtoToPassengerRating(passengerRatingPatchDto)));
    }
}