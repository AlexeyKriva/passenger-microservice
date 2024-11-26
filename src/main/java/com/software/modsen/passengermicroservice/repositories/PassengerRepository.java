package com.software.modsen.passengermicroservice.repositories;

import com.software.modsen.passengermicroservice.entities.Passenger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface PassengerRepository extends ReactiveMongoRepository<Passenger, String> {
    Mono<Passenger> findPassengerByIdAndDeleted(String id, boolean isDeleted);

    Mono<Boolean> existsByIdAndDeleted(String id, boolean isDeleted);

    Mono<Passenger> findByName(String name);
}