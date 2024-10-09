package com.software.modsen.passengermicroservice.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passenger")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Passenger entity.")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    @Schema(example = "user@gmail.com")
    private String email;

    @Column(name = "phone_number", nullable = false)
    @Schema(example = "+375331234567")
    private String phoneNumber;

    @Column(name = "is_deleted", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean isDeleted;
}