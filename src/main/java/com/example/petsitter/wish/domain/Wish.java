package com.example.petsitter.wish.domain;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.petsitter.domain.Petsitter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "wish",
        uniqueConstraints = {
        @UniqueConstraint(name = "UK_wish_petsitter_member", columnNames = {"petsitter_id", "member_id"})
})
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private Petsitter petsitter;


    @Builder
    public Wish(Long id, Member member, Petsitter petsitter) {
        this.id = id;
        this.member = member;
        this.petsitter = petsitter;
    }
}
