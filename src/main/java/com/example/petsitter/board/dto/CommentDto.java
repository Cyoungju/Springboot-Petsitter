package com.example.petsitter.board.dto;


import com.example.petsitter.board.domain.Board;
import com.example.petsitter.board.domain.Comment;
import com.example.petsitter.member.domain.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    private String contents;

    private LocalDateTime createTime;

    private Long boardId;

    private String memberEmail;

    public Comment toEntity(Board board, Member member) {
        return Comment.builder()
                .contents(contents)
                .createTime(createTime)
                .board(board)
                .member(member)
                .build();
    }

    public static CommentDto tocommentDTO(Comment comment,Long boardId){
        return new CommentDto(
                comment.getId(),
                comment.getContents(),
                comment.getCreateTime(),
                boardId,
                comment.getMember().getEmail()
        );

    }
}
