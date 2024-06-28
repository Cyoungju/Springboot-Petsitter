package com.example.petsitter.wish;

import com.example.petsitter.petsitter.domain.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish,Long> {
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);

    boolean existsByMemberIdAndPetsitterId(Long memberId, Long petsitterId);
}
