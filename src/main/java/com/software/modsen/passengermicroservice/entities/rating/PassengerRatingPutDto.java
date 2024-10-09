package com.software.modsen.passengermicroservice.entities.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
@ToString
public class PassengerRatingPutDto {
    @NotNull(message = "Passenger id cannot be null.")
    @JsonProperty("passenger_id")
    private Long passengerId;

    @NotNull(message = "Rating value cannot be null.")
    @Range(min = 0, max = 5, message = "Rating value must be between 0 and 5.")
    @JsonProperty("rating_value")
    private Float ratingValue;

    @NotNull(message = "Number of ratings cannot be null.")
    @JsonProperty("number_of_ratings")
    private Integer numberOfRatings;
}
