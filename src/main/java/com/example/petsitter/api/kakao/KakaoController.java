package com.example.petsitter.api.kakao;


import com.example.petsitter.member.dto.MemberDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@RequiredArgsConstructor
@Controller
@Log4j2
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/api/kakao/callback")
    public RedirectView redirect(@RequestParam(name = "code", required = false) String code , HttpServletRequest request){
        if (code != null) {
            // Handle the code parameter
            System.out.println("Received code: " + code);
            String accessToken = kakaoService.getAccessToken(code);
            MemberDto user = kakaoService.getKakaoMember(accessToken);
            kakaoService.authenticateUser(user);

            // 인증 정보를 세션에 저장
            SecurityContext securityContext = SecurityContextHolder.getContext();
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            log.info(accessToken);

            // 토큰 세션에 저장
            session.setAttribute("accessToken", accessToken);

            System.out.println("로그인 accessToken" + session.getAttribute("accessToken"));

            // 로그인 처리가 완료된 후 홈 페이지로 리디렉션
            return new RedirectView("/");
        }

        // 코드가 없을 경우 로그인 페이지로 리디렉션
        return new RedirectView("/login");
    }


    @GetMapping("/api/kakao/logout")
    public RedirectView kakaoLogout(HttpServletRequest request, HttpSession session){

        // 세션에서 토큰 가져오기
        String accessToken = (String) session.getAttribute("accessToken");

        // 카카오 로그아웃
        kakaoService.kakaoLogout(accessToken);

        session.invalidate();
        SecurityContextHolder.clearContext();

        // 세션 속성 지우기
        //session.removeAttribute("accessToken");

        // 로그아웃 처리가 완료된 후 홈 페이지로 리디렉션
        return new RedirectView("/");

    }
}
