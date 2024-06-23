package com.example.petsitter.member.dto;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.domain.MemberImage;
import com.example.petsitter.member.domain.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class MemberDto {
    private Long id;

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

    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    // 업로드시 사용
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    //이미 업로드 되어있는 파일들의 이름
    @Builder.Default
    private List<String> uploadedFileName = new ArrayList<>();

    public MemberDto(Long id, String email, String password, String nickname, String userTel, boolean social, String address, String detailAdr, List<MemberRole> memberRoleList, List<String> uploadedFileName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userTel = userTel;
        this.social = social;
        this.address = address;
        this.detailAdr = detailAdr;
        this.memberRoleList = memberRoleList;
        this.uploadedFileName = uploadedFileName;
    }

    public MemberDto(String email, String password , boolean social) {
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

        log.info("uploadedFileName : " + uploadedFileName);

        if(uploadedFileName == null){
            log.info("uploadedFileName Null : "+ uploadedFileName);
            return member;
        }

        uploadedFileName.stream().forEach(uploadName -> {
            member.addImageString(uploadName);
        });

        return member;
    }

    public static MemberDto tomemberDto(Member member){
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.getUserTel(),
                member.isSocial(),
                member.getAddress(),
                member.getDetailAdr(),
                member.getMemberRoleList(),
                member.getImageList().stream().map(
                    images-> images.getFileName()
                ).collect(Collectors.toList())
        );
    }


}
