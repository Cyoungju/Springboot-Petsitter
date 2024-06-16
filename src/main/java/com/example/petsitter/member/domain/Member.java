package com.example.petsitter.member.domain;

import com.example.petsitter.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@ToString(exclude = "memberRoleList")
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String userTel;

    private boolean social;

    private String address;

    private String detailAdr;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole memberRole){
        memberRoleList.add(memberRole);
    }

    public void clearRole(){
        memberRoleList.clear();
    }

    @Builder
    public Member(Long id, String email, String password, String nickname, String userTel, boolean social, String address, String detailAdr, List<MemberRole> memberRoleList) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userTel = userTel;
        this.social = social;
        this.address = address;
        this.detailAdr = detailAdr;
        this.memberRoleList = memberRoleList;
    }

    @Transient // 암호를 암호화로 저장하는 것이기 때문에
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void updateFromDTO(MemberDto memberDto){
        // 모든 변경 사항을 셋팅. =>  기존에 있는 데이터에 저장해야하기 때문에 new 객체 생성을 하는 toEntity 사용 불가
        this.password = passwordEncoder.encode(memberDto.getPassword());
        this.nickname = memberDto.getNickname();
        this.userTel = memberDto.getUserTel();
        this.address = memberDto.getAddress();
        this.detailAdr = memberDto.getDetailAdr();
    }
}
