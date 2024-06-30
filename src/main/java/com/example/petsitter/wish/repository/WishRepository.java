package com.example.petsitter.wish.repository;

import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.wish.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish,Long> {
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);

    boolean existsByMemberIdAndPetsitterId(Long memberId, Long petsitterId);

    Optional<Wish> findByPetsitterIdAndMemberId(Long petsitterId, Long memberId);

    List<Wish> findByPetsitterId(Long id);
}
