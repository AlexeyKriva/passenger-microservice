package com.software.modsen.passengermicroservice.entities.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
@ToString
public class PassengerRatingPatchDto {
    @JsonProperty("passenger_id")
    private Long passengerId;

    @Range(min = 0, max = 5, message = "Rating value must be between 0 and 5.")
    @JsonProperty("rating_value")
    private Float ratingValue;

    @JsonProperty("number_of_ratings")
    private Integer numberOfRatings;
}
