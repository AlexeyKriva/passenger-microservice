package com.software.modsen.passengermicroservice.entities.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Range;

@Schema(description = "Entity to update passenger balance.")
public record PassengerRatingPatchDto(
        @Range(min = 0, max = 5, message = "Rating value must be between 0 and 5.")
        @JsonProperty("ratingValue")
        Float ratingValue,

        @JsonProperty("numberOfRatings")
        Integer numberOfRatings
) {
}
