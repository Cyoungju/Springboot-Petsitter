package com.example.petsitter.member.controller;

import com.example.petsitter.api.kakao.KakaoService;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.core.util.CustomFileUtil;
import com.example.petsitter.member.dto.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private KakaoService kakaoService;

    @Mock
    private CustomFileUtil fileUtil;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    void testMainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("kakaoLoginLink"));
    }

    @Test
    void testJoinPage() throws Exception {
        mockMvc.perform(get("/join"))
                .andExpect(status().isOk())
                .andExpect(view().name("join"))
                .andExpect(model().attributeExists("memberDto"))
                .andExpect(model().attributeExists("check"))
                .andExpect(model().attribute("check", false));
    }

    @Test
    void testJoinProcess_validMemberDto() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail("test@example.com");
        memberDto.setPassword("password123");

        mockMvc.perform(post("/joinProc")
                        .param("email", memberDto.getEmail())
                        .param("password", memberDto.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(memberService, times(1)).joinProcess(any(MemberDto.class));
    }

    @Test
    void testJoinProcess_invalidMemberDto() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail("test@example.com"); // Email is missing password

        mockMvc.perform(post("/joinProc")
                        .param("email", memberDto.getEmail()))
                .andExpect(status().isOk())
                .andExpect(view().name("join"));

        verify(memberService, never()).joinProcess(any(MemberDto.class));
    }

    @Test
    void testIdCheck_existingEmail() throws Exception {
        String existingEmail = "existing@example.com";
        when(memberService.idCheck(existingEmail)).thenReturn("이미 사용중인 아이디 입니다!");

        mockMvc.perform(get("/idcheck")
                        .param("email", existingEmail))
                .andExpect(status().isOk())
                .andExpect(content().string("이미 사용중인 아이디 입니다!"));

        verify(memberService, times(1)).idCheck(existingEmail);
    }

    @Test
    void testIdCheck_nonExistingEmail() throws Exception {
        String nonExistingEmail = "new@example.com";
        when(memberService.idCheck(nonExistingEmail)).thenReturn("사용 가능합니다!");

        mockMvc.perform(get("/idcheck")
                        .param("email", nonExistingEmail))
                .andExpect(status().isOk())
                .andExpect(content().string("사용 가능합니다!"));

        verify(memberService, times(1)).idCheck(nonExistingEmail);
    }


}