package com.software.modsen.passengermicroservice.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "passengers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Passenger entity.")
public class Passenger {
    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String name;

    @Schema(example = "user@gmail.com")
    private String email;

    @Schema(example = "+375331234567")
    private String phoneNumber;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean isDeleted;
}