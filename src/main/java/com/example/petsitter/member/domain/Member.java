package com.example.petsitter.member.domain;

import jakarta.persistence.*;
import lombok.*;

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
}
