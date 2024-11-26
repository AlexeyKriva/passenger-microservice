package com.software.modsen.passengermicroservice.entities.account;

import com.software.modsen.passengermicroservice.entities.Passenger;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Passenger account entity.")
public class PassengerAccount {
    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String passengerId;

    private Float balance;

    private Currency currency;

    @Version
    private Long version;
}