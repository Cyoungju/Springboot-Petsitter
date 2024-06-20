package com.example.petsitter.member.controller;

import com.example.petsitter.api.kakao.KakaoService;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.core.util.CustomFileUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    private final KakaoService kakaoService;
    private final CustomFileUtil fileUtil;


    @GetMapping("/")
    public String mainP(){
        return "index";
    }

    @GetMapping("/login")
    public String loginP(Model model){
        String kakaoLoginLink = kakaoService.getKakaoLoginLink();
        model.addAttribute("kakaoLoginLink", kakaoLoginLink);
        log.info(kakaoLoginLink);
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
        return "redirect:/login";
    }

    @GetMapping("/idcheck")
    public @ResponseBody String idcheck(@RequestParam String email){
        String result = memberService.idCheck(email);
        return result;
    }


    @PostMapping("/mypage")
    public String updateMember(@ModelAttribute("member") MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "update";
        }
        memberService.updateMember(memberDto);
        // 회원 정보 업데이트 로직
        return "redirect:/";
    }


    @GetMapping("/update")
    public String updateP(Model model){
        String username = memberService.getAuthName();
        MemberDto memberDto = memberService.findByDtoEmail(username);

        log.info(memberDto.getMemberRoleList());
        model.addAttribute("memberDto", memberDto);
        return "update";
    }

    @GetMapping("/member/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        return fileUtil.getFile(fileName);
    }

}
