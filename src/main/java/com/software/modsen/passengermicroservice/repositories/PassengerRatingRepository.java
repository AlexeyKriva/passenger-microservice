package com.software.modsen.passengermicroservice.repositories;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRatingRepository extends JpaRepository<PassengerRating, Long> {
    Optional<PassengerRating> findByPassengerId(long passengerId);
}
