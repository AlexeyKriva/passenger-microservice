package com.software.modsen.passengermicroservice.entities.rating;

import com.software.modsen.passengermicroservice.entities.Passenger;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "ratings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Passenger rating entity.")
public class PassengerRating {
    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String passengerId;

    private Float ratingValue;

    private Integer numberOfRatings;
}