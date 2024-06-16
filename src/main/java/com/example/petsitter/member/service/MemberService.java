package com.example.petsitter.member.service;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.MemberDto;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberService {
    void joinProcess(MemberDto memberDto);

    String idCheck(String email);

    void updateMember(MemberDto memberDto);

    Member findByEmail(String username);

    void loginProcess(MemberDto memberDto);
}
