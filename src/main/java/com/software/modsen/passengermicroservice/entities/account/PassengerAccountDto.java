package com.software.modsen.passengermicroservice.entities.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
public class PassengerAccountDto {
    @NotNull
    @Range(min = 5, message = "You cannot to raise your balance less than 5.")
    @JsonProperty("balance")
    private Float balance;

    @NotNull
    @JsonProperty("currency")
    private Currency currency;
}
