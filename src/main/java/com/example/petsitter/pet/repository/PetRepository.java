package com.example.petsitter.pet.repository;

import com.example.petsitter.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long>{

    List<Pet> findByMemberId(Long id);

}
