package com.example.petsitter.member.service;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member userData = memberRepository.findByEmail(username);
        if(userData != null){
            return new CustomUserDetails(userData);
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
