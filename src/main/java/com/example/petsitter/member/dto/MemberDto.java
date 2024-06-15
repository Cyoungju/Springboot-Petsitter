package com.example.petsitter.member.dto;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.domain.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String nickname;

    private String userTel;

    private boolean social;

    private String address;

    private String detailAdr;

    private List<MemberRole> memberRoleList = new ArrayList<>();

    public MemberDto(String email, String password, boolean social) {
        this.email = email;
        this.password = password;
        this.social = social;
    }

    @Builder
    public Member toEntity() {
        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .userTel(userTel)
                .address(address)
                .detailAdr(detailAdr)
                .social(false)
                .build();

        // 기본 사용자 - defalt
        member.addRole(MemberRole.USER);

        return member;
    }

}
