package com.example.petsitter.member.controller;

import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.repository.MemberRepository;
import com.example.petsitter.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testJoin(){
        MemberDto memberDto = new MemberDto();

        memberDto.setEmail("test@admin.com");
        memberDto.setPassword("pass1234@");
        memberDto.setNickname("테스트");

        memberService.joinProcess(memberDto);
    }

    @Test
    public void testLoginWithRegisteredUser() throws Exception {
        //testJoin();

        // 로그인 테스트 코드 추가
        mockMvc.perform(post("/loginProc")
                        .param("username", "test@admin.com")
                        .param("password", "pass1234@")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testLoginPageLoads() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testLoginWithValidUser() throws Exception {
        mockMvc.perform(formLogin("/loginProc").user("validUsername").password("validPassword"))
                .andExpect(authenticated());
    }

    @Test
    public void testLoginWithInvalidUser() throws Exception {
        mockMvc.perform(formLogin("/loginProc").user("invalidUsername").password("invalidPassword"))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

}