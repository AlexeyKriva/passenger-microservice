package com.software.modsen.passengermicroservice.repositories;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface PassengerRatingRepository extends ReactiveMongoRepository<PassengerRating, String> {
    Mono<PassengerRating> findByPassengerId(String passengerId);
}
