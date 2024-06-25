package com.example.petsitter.petsitter.repository;

import com.example.petsitter.petsitter.domain.PetsitterReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetsitterReservationRepository extends JpaRepository<PetsitterReservation, Long> {
}
