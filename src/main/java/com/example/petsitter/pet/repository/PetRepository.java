package com.example.petsitter.pet.repository;

import com.example.petsitter.pet.domain.Pet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long>{

    @EntityGraph(attributePaths = "imageList")
    @Query("SELECT p FROM Pet p WHERE p.id = :id")
    Optional<Pet> selectOne(@Param("id") Long id);

    List<Pet> findByMemberEmail(String username);

    List<Pet> findByMemberId(Long id);
}
