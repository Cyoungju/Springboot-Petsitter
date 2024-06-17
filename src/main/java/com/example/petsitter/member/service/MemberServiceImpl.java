package com.example.petsitter.member.service;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public void joinProcess(MemberDto memberDto) {
        //db에 동일한 username이 존재하는지 확인
        boolean isUser = memberRepository.existsByEmail(memberDto.getEmail());

        if(isUser){
            return;
        }

        //비밀번호 인코딩
        String encodedPassword = bCryptPasswordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encodedPassword);
        memberRepository.save(memberDto.toEntity());
    }

    @Override
    public String idCheck(String email) {
        boolean isUser = memberRepository.existsByEmail(email);
        boolean check = false;

        String msg= "";
        if(isUser){
            msg = "이미 사용중인 아이디 입니다!";
        }else {
            msg = "사용 가능합니다!";
            check = true;
        }

        return msg;
    }

    @Override
    public void updateMember(MemberDto memberDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(username);
        log.info(member + "member");
        member.updateFromDTO(memberDto);

        log.info(memberDto + "memberDto");

        memberRepository.save(member);
    }

    @Override
    public Member findByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        return member;
    }

    @Override
    public void loginProcess(MemberDto memberDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(memberDto.getEmail(), memberDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                usernamePasswordAuthenticationToken
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        System.out.println(customUserDetails.getUsername());
    }

    @Override
    public String getAuthName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }


}
