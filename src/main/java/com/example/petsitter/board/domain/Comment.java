package com.example.petsitter.board.domain;


import com.example.petsitter.board.dto.CommentDto;
import com.example.petsitter.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Entity
@Table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300)
    private String contents;

    private LocalDateTime createTime;

    //비소유 했지만 확인 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Comment(Long id, String contents,LocalDateTime createTime, Board board, Member member) {
        this.id = id;
        this.contents = contents;
        this.createTime = createTime;
        this.board = board;
        this.member = member;
    }


    //DTO -> Entity
    public void updateCommentFromDTO(CommentDto commentDTO) {
        this.contents = commentDTO.getContents();
    }

}

