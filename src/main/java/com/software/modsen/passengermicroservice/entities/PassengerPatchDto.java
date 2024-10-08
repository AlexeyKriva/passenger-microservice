package com.software.modsen.passengermicroservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PassengerPatchDto {
    @JsonProperty("name")
    @Size(min = 4, max = 55, message = "The number of characters in the name" +
            " must be at least 4 and not exceed 55.")
    private String name;
    @JsonProperty("email")
    @Email(message = "Invalid email format.")
    private String email;
    @JsonProperty("phone_number")
    @Pattern(regexp = "^(?:\\+375|375|80)(?:25|29|33|44|17)\\d{7}$\n", message = "Invalid phone" +
            " number format")
    private String phoneNumber;
}