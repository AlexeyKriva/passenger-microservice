package com.software.modsen.passengermicroservice.entities.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Entity to update passenger rating.")
public class PassengerRatingMessage {
        @NotNull(message = "Passenger id cannot be null.")
        @JsonProperty("passengerId")
        private Long passengerId;

        @NotNull(message = "Rating value cannot be null.")
        @Range(min = 1, max = 5, message = "Rating value must be between 1 and 5.")
        @JsonProperty("ratingValue")
        private Integer ratingValue;
}