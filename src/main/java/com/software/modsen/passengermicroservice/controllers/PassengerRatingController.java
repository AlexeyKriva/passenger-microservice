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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<PassengerRating> getAllPassengerRatings(@RequestParam(name = "includeDeleted",
            required = false, defaultValue = "true")
                                                                            boolean includeDeleted) {
        return passengerRatingService.getAllPassengerRatings(includeDeleted);
    }

    @GetMapping("/ratings/{id}")
    @Operation(
            description = "Allows to get passenger rating by id."
    )
    public Mono<PassengerRating> getPassengerRatingById(
            @PathVariable("id")
            @Parameter(description = "Passenger rating id.")
            String id) {
        return passengerRatingService.getPassengerRatingById(id);
    }

    @GetMapping("/{passenger_id}/ratings")
    @Operation(
            description = "Allows to get passenger rating by passenger id."
    )
    public Mono<PassengerRating> getPassengerRatingByPassengerId(
            @PathVariable("passenger_id") @Parameter(description = "Passenger id.")
            String id) {
        return passengerRatingService.getPassengerRatingByPassengerId(id);
    }

    @PutMapping("/ratings/{id}")
    @Operation(
            description = "Allows to update passenger rating by id."
    )
    public Mono<PassengerRating> putPassengerRatingById(@PathVariable("id") String id,
                                                                  @Valid @RequestBody
                                                                  @Parameter(description = "Passenger rating entity.")
                                                                  PassengerRatingPutDto passengerRatingPutDto) {
        return passengerRatingService.putPassengerRatingById(
                id,
                PASSENGER_RATING_MAPPER.fromPassengerRatingPutDtoToPassengerRating(passengerRatingPutDto));
    }

    @PatchMapping("/ratings/{id}")
    @Operation(
            description = "Allows to update passenger rating by id."
    )
    public Mono<PassengerRating> patchPassengerRatingById(@PathVariable("id") String id,
                                                                   @Valid @RequestBody
                                                                   @Parameter(description = "Passenger rating entity.")
                                                                   PassengerRatingPatchDto passengerRatingPatchDto) {
        return passengerRatingService.patchPassengerRatingById(
                id,
                PASSENGER_RATING_MAPPER.fromPassengerRatingPatchDtoToPassengerRating(passengerRatingPatchDto));
    }
}