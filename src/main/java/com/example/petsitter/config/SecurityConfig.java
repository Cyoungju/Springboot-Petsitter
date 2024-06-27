package com.example.petsitter.config;

//import com.example.petsitter.api.kakao.KakaoAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers("/","/login","/loginProc","/join","/joinProc","/images/**","/css/**", "/idcheck","/api/**", "/api/kakao/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/my/**", "/pet/**","/pet/view/**", "/pet/delete/**", "/pet/update/**", "/petsitter/view/**", "/petsitter/**","/reservation/**","/addAddress").hasAnyRole("ADMIN","USER","MANAGER") // ** 와일드카드
                        .requestMatchers("/petsitter/sitterRole/create", "/my/myPetsitterList").hasAnyRole("MANAGER","ADMIN")
                        .anyRequest().authenticated() //나머지 로그인한 사용자만 접근
                );
        http
                .formLogin((auth)-> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc").permitAll());
        // html form 태그에 action - 자동으로 넘겨줌

        http
                .logout((auth)->auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/"));
        http
                .csrf((auth)->auth.disable());
        // 다중 로그인 설정
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());
        //changeSessionId : 로그인 시 동일한 세션에 대한 id 변경

        return http.build();
    }
}