package com.example.petsitter.api.kakao;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.domain.MemberRole;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.repository.MemberRepository;
import com.example.petsitter.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.LinkedHashMap;


@Log4j2
@RequiredArgsConstructor
@Service
public class KakaoService {
    private final KakaoURI kakaoURI;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;

    private final CustomUserDetailsService userDetailsService;


    // 1. 인가코드 url주소 - 버튼 클릭 주소 만들기
    public String getKakaoLoginLink(){
        String auth_code_path = "https://kauth.kakao.com/oauth/authorize";
        String kakaoUrl = auth_code_path + "?response_type=code&client_id="+kakaoURI.getRest_api_key()+"&redirect_uri="+kakaoURI.getRedirect_uri();
        return kakaoUrl;
    }


    // 2. 토큰 받기
    //accessToken url 주소
    private String getAccessTokenLink(String code){
        String access_token_url = "https://kauth.kakao.com/oauth/token";
        String accessTokenUri = access_token_url +"?grant_type=authorization_code&client_id="+kakaoURI.getRest_api_key()+"&redirect_uri="+kakaoURI.getRedirect_uri()+"&code="+code;
        return accessTokenUri;
    }

    public String getAccessToken(String code){
        String getAccessTokenLink = getAccessTokenLink(code);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(getAccessTokenLink).build();

        ResponseEntity<LinkedHashMap> response
                = restTemplate.exchange(uriBuilder.toUri(), HttpMethod.POST, entity, LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info(bodyMap.get("access_token"));

        String accessToken = String.valueOf(bodyMap.get("access_token"));

        return accessToken;
    }

    // 3. 사용자 정보 받아 오기 - get
    private LinkedHashMap<String, LinkedHashMap> getEmailFromKakaoAccessToken(String accessToken){
        String kakaoGetUserUrl = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserUrl).build();

        ResponseEntity<LinkedHashMap> response
                = restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, LinkedHashMap.class);
        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        LinkedHashMap<String , LinkedHashMap> kakaoAcount = bodyMap.get("kakao_account");
        log.info("kakao_account"+ kakaoAcount);
        return kakaoAcount;
    }

    // 3. 사용자 정보 받아 오기 - 이메일로 받아오기
    public MemberDto getKakaoMember(String accessToken){

        LinkedHashMap<String, LinkedHashMap> kakaoAcount = getEmailFromKakaoAccessToken(accessToken);
        //카카오 연동 - 이메일 받아오기
        String email = String.valueOf(kakaoAcount.get("email"));


        LinkedHashMap<String, LinkedHashMap> profileMap = kakaoAcount.get("profile");
        String nickname = String.valueOf(profileMap.get("nickname"));
        String thumbUrl = String.valueOf(profileMap.get("thumbnail_image_url"));

        log.info("nickname"+nickname);
        log.info("email"+email);
        log.info("thumbUrl" + thumbUrl);

        // 현재 데이터에서 확인후 처리
        Member result = memberRepository.findByEmail(email);

        if(result != null){ // 회원일 경우
            MemberDto user = entityToDTO(result);
            log.info("existed........" + user);

            return user;
        }

        //만약 일치하는 아이디가 없을경우 - 소셜 아이디로 만들어줌
        Member socialUserEntity = makeSocialUser(email, nickname, thumbUrl);
        memberRepository.save(socialUserEntity);


        // 새로 생성된 사용자의 경우 로그인 처리
        MemberDto resultDto = entityToDTO(socialUserEntity);

        return resultDto;
    }


    public void kakaoLogout(String accessToken){
        String kakaoGetUserUrl = "https://kapi.kakao.com/v1/user/logout";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserUrl).build();

        ResponseEntity<LinkedHashMap> response
                = restTemplate.exchange(uriBuilder.toUri(), HttpMethod.POST, entity, LinkedHashMap.class);

        log.info("logout" + response);
    }


    // 사용자 정보 만들기
    private Member makeSocialUser(String email, String nickname, String thumbUrl){
        String tempPassword = makeSocialPassword();
        log.info("tempPassword"+ tempPassword);
        //List<MemberRole> memberRoleList = new ArrayList<>();

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        if(thumbUrl == null){
            return member;
        }

        member.addImageString(thumbUrl);


        return member;
    }

    // 패스워드 임의 설정
    private String makeSocialPassword(){
        StringBuffer buffer = new StringBuffer();

        for(int i = 0; i <= 10;i++){
            buffer.append((char)((int)(Math.random()*55) +65));
        }
        return buffer.toString();
    }

    private MemberDto entityToDTO(Member user) {

        MemberDto dto = new MemberDto(
                user.getEmail(),
                user.getPassword(),
                user.isSocial()
        );
        return dto;
    }


    // 로그인 인증 작업
    public void authenticateUser(MemberDto memberDto) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(memberDto.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("======= username,로그인진행중 =======" + username);
    }

}