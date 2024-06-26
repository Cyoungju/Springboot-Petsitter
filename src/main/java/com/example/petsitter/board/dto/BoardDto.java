package com.example.petsitter.board.dto;

import com.example.petsitter.board.domain.Board;
import com.example.petsitter.member.domain.Member;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;

    private String title;

    private String contents;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Member member;

    private String memberEmail;

    public Board toEntity(Member member) {
        return Board.builder()
                .title(title)
                .contents(contents)
                .member(member)
                .createTime(createTime)
                .updateTime(LocalDateTime.now()) //수정된 시간
                .build();
    }

    //toEntity -> DTO로 변환
    public static BoardDto toboardDTO(Board board){
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime(),
                board.getMember(),
                board.getMember().getEmail()
        );
    }
}
