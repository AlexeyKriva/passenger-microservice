package com.software.modsen.passengermicroservice.repositories;

import com.software.modsen.passengermicroservice.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findPassengerByIdAndIsDeleted(long id, boolean isDeleted);

    boolean existsByIdAndIsDeleted(long id, boolean isDeleted);
}