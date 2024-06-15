package com.example.petsitter.api.kakao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoURI {

    @Value("${kakao.api.key}")
    private String rest_api_key;

    @Value("${kakao.redirect.uri}")
    private String redirect_uri;
}
