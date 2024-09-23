package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPatchDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPutDto;
import com.software.modsen.passengermicroservice.services.PassengerRatingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/passenger/rating", produces = "application/json")
@AllArgsConstructor
public class PassengerRatingController {
    private PassengerRatingService passengerRatingService;

    @GetMapping
    public ResponseEntity<List<PassengerRating>> getAllPassengerRatings() {
        return ResponseEntity.ok(passengerRatingService.getAllPassengerRatings());
    }

    @GetMapping("/not-deleted")
    public ResponseEntity<List<PassengerRating>> getAllPassengerRatingsAndNotDeleted() {
        return ResponseEntity.ok(passengerRatingService.getAllPassengerRatingsAndNotDeleted());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerRating> getPassengerRatingById(@PathVariable("id") long id) {
        return ResponseEntity.ok(passengerRatingService.getPassengerRatingById(id));
    }

    @GetMapping("/{passenger_id}/not-deleted")
    public ResponseEntity<PassengerRating> getPassengerRatingByPassengerIdAndNotDeleted(@PathVariable("passenger_id") long id) {
        return ResponseEntity.ok(passengerRatingService.getPassengerRatingByIdAndNotDeleted(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerRating> putPassengerRatingById(@PathVariable("id") long id,
                                                                  @Valid @RequestBody
                                                                  PassengerRatingPutDto passengerRatingPutDto) {
        return ResponseEntity.ok(passengerRatingService.putPassengerRatingById(id, passengerRatingPutDto));
    }

    @PatchMapping
    public ResponseEntity<PassengerRating> updatePassengerRating(@Valid
                                                                 @RequestBody PassengerRatingDto passengerRatingDto) {
        return ResponseEntity.ok(passengerRatingService.updatePassengerRating(passengerRatingDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PassengerRating> patchPassengerRatingById(@PathVariable("id") long id,
                                                                   @Valid @RequestBody
                                                                   PassengerRatingPatchDto passengerRatingPatchDto) {
        return ResponseEntity.ok(passengerRatingService.patchPassengerRatingById(id, passengerRatingPatchDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePassengerRatingById(@PathVariable("id") long id) {
        passengerRatingService.deletePassengerRatingById(id);
        return ResponseEntity.ok("Passenger rating was successfully deleted by id " + id);
    }
}