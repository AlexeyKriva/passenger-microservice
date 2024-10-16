package com.software.modsen.passengermicroservice.entities.rating;

import com.software.modsen.passengermicroservice.entities.Passenger;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passenger_rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Passenger rating entity.")
public class PassengerRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @OneToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @Column(name = "rating_value", nullable = false)
    private Float ratingValue;

    @Column(name = "number_of_ratings", nullable = false)
    private Integer numberOfRatings;
}
