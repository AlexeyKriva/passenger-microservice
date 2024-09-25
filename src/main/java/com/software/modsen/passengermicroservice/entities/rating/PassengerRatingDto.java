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
public class PassengerRatingDto {
    @NotNull(message = "Passenger id cannot be null.")
    @JsonProperty("passenger_id")
    private Long passengerId;

    @NotNull(message = "Rating value cannot be null.")
    @Range(min = 1, max = 5, message = "Rating value must be between 1 and 5.")
    @JsonProperty("rating_value")
    private Integer ratingValue;
}
