package com.example.petsitter.member.controller;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/my")
public class MyController {

    private final MemberService memberService;
    private final PetService petService;

    @GetMapping("/mypage")
    public String updateP(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.findByEmail(username);

        log.info(member);
        model.addAttribute("memberDto", member);
        return "update";
    }


    @GetMapping("/myPetlist")
    public String list(Model model){
        List<PetDto> petDtoList = petService.getList();
        model.addAttribute("list", petDtoList);
        return "/pet/list";
    }




}
