package com.software.modsen.passengermicroservice.repositories;

import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerAccountRepository extends JpaRepository<PassengerAccount, Long> {
    Optional<PassengerAccount> findByPassengerId(long passengerId);
}
