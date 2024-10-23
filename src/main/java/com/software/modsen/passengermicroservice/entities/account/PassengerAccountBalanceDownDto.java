package com.software.modsen.passengermicroservice.entities.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

@Schema(description = "Entity to down passenger balance.")
public record PassengerAccountBalanceDownDto(
        @NotNull
        @Range(min = 0, message = "You cannot to reduce your balance less than 0.")
        @JsonProperty("balance")
        Float balance,

        @NotNull
        @JsonProperty("currency")
        Currency currency
) {
}
