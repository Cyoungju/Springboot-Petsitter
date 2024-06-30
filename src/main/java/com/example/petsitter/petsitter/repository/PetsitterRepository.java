package com.example.petsitter.petsitter.repository;

import com.example.petsitter.petsitter.domain.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetsitterRepository extends JpaRepository<Petsitter,Long> {

    List<Petsitter> findByMemberId(Long id);

    Page<Petsitter> findAllByDelFlagFalse(PageRequest id);

    Page<Petsitter> findBySitterWorkAdrContainingIgnoreCaseAndDelFlagFalse(String searchKeyword, PageRequest id);
}
