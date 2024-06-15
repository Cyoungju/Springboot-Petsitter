package com.example.petsitter.member.controller;

import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    @ModelAttribute
    public void addCommonModelAttributes(Model model) {
        // 세션 현재 사용자 아이디
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        log.info("사용자 아이디" + username);
    }


    @GetMapping("/")
    public String mainP(){
        return "index";
    }

    @GetMapping("/login")
    public String loginP(Model model){
        return "login";
    }

    @GetMapping("/join")
    public String joinP(Model model){
        model.addAttribute("memberDto", new MemberDto());
        model.addAttribute("check", false);
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(@Valid MemberDto memberDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "join";
        }

        try {
            memberService.joinProcess(memberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "join";
        }
        return "/login";
    }

    @GetMapping("/idcheck")
    public @ResponseBody String idcheck(@RequestParam String email){
        String result = memberService.idCheck(email);
        return result;
    }
}
