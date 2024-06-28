package com.example.petsitter.pet.repository;

import com.example.petsitter.pet.domain.Pet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long>{

    List<Pet> findByMemberId(Long id);

}
