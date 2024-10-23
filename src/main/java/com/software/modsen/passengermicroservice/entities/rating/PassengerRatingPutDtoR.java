package com.software.modsen.passengermicroservice.entities.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

@Schema(description = "Entity to update passenger balance.")
public record PassengerRatingPutDtoR(
        @NotNull(message = "Rating value cannot be null.")
        @Range(min = 0, max = 5, message = "Rating value must be between 0 and 5.")
        @JsonProperty("ratingValue")
        Float ratingValue,

        @NotNull(message = "Number of ratings cannot be null.")
        @JsonProperty("numberOfRatings")
        Integer numberOfRatings
) {
}
