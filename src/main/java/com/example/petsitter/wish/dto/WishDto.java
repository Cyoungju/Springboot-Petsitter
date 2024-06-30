package com.example.petsitter.wish.dto;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.wish.domain.Wish;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {

    private Long id;

    private Member member;

    private Petsitter petsitter;


    public Wish toEntity(Member member, Petsitter petsitter) {
        Wish wish = Wish.builder()
                .id(id)
                .member(member)
                .petsitter(petsitter)
                .build();
        return wish;
    }

    public static WishDto towishDTO(Wish wish) {
        return new WishDto(
                wish.getId(),
                wish.getMember(),
                wish.getPetsitter()
        );
    }
}
