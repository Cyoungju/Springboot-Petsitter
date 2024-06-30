package com.example.petsitter.wish.controller;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/wish")
@RequiredArgsConstructor
@Controller
public class WishController {

    private final WishService wishService;
    private final MemberService memberService;

    @PostMapping("/")
    public ResponseEntity<String> addWish(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam Long petsitterId){
        String username = customUserDetails.getUsername();
        wishService.addWish(username, petsitterId);

        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkWishlist(@RequestParam Long petsitterId) {
        String username = memberService.getAuthName();
        Member member = memberService.findByEmail(username);  // MemberServiceImpl에 해당 메서드가 있다고 가정합니다.
        Long memberId = member.getId();
        boolean exists = wishService.isWishExist(memberId, petsitterId);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        wishService.delete(id);
        return ResponseEntity.ok("SUCCESS");

    }

}
