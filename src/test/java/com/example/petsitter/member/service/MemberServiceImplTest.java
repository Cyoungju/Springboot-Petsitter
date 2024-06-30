package com.example.petsitter.member.service;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.domain.MemberRole;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.repository.MemberRepository;
import com.example.petsitter.core.util.CustomFileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private CustomFileUtil fileUtil;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testJoinProcess() {
        // Given
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail("test@example.com");
        memberDto.setPassword("password123");
        memberDto.setFiles(new ArrayList<>()); // Replace with actual files if needed

        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        memberService.joinProcess(memberDto);

        // Then
        verify(memberRepository, times(1)).existsByEmail("test@example.com");
        verify(bCryptPasswordEncoder, times(1)).encode("password123");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testIdCheck_existingEmail() {
        // Given
        String existingEmail = "existing@example.com";
        when(memberRepository.existsByEmail(existingEmail)).thenReturn(true);

        // When
        String result = memberService.idCheck(existingEmail);

        // Then
        assertEquals("이미 사용중인 아이디 입니다!", result);
    }

    @Test
    void testIdCheck_nonExistingEmail() {
        // Given
        String nonExistingEmail = "new@example.com";
        when(memberRepository.existsByEmail(nonExistingEmail)).thenReturn(false);

        // When
        String result = memberService.idCheck(nonExistingEmail);

        // Then
        assertEquals("사용 가능합니다!", result);
    }


}