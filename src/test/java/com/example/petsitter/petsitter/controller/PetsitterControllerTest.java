package com.example.petsitter.petsitter.controller;

import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.service.PetsitterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
class PetsitterControllerTest {

    @Autowired
    private PetsitterService petsitterService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void resisterTest() throws Exception{
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        PetsitterDto petsitterDto = new PetsitterDto();

        petsitterDto.setSitterName("펫시터 이름");
        petsitterDto.setSitterContent("펫시터 내용");
        petsitterDto.setSitterPrice(30000L);
        petsitterDto.setCreateTime(LocalDateTime.now());
        petsitterDto.setUpdateTime(LocalDateTime.now());

        petsitterService.save(petsitterDto);
    }

}