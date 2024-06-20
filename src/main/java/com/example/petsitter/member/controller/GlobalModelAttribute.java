package com.example.petsitter.member.controller;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalModelAttribute {
    @Autowired
    private MemberService memberService;

    @ModelAttribute
    public void addCommonModelAttributes(Model model) {
        // 세션 현재 사용자 아이디
        String username = memberService.getAuthName();
        model.addAttribute("username", username);

        if (username.equals("anonymousUser")) { //비회원
            model.addAttribute("isSocial", false);
        }else {
            Member member = memberService.findByEmail(username);
            model.addAttribute("isSocial", member.isSocial());

            // System.out.print("권한 : ");
            List<String> roles = member.getMemberRoleList().stream()
                    .map(role -> role.name())
                    .collect(Collectors.toList());

            model.addAttribute("role", roles);
        }

    }
}
