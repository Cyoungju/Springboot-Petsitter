package com.example.petsitter.petsitter.repository;

import com.example.petsitter.petsitter.domain.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetsitterRepository extends JpaRepository<Petsitter,Long> {
    Page<Petsitter> findBySitterWorkAdrContainingIgnoreCase(String sitterWorkAdr, Pageable pageable);
}
