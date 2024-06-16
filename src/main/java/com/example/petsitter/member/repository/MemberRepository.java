package com.example.petsitter.member.repository;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByEmail(String email);
    Member findByEmail(String email);

}
