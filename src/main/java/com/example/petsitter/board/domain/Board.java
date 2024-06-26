package com.example.petsitter.board.domain;


import com.example.petsitter.board.dto.BoardDto;
import com.example.petsitter.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    // ** 작성 시간
    private LocalDateTime createTime;

    // ** 최근 수정 시간
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comment = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public Board(Long id, String title, String contents,Member member, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.member = member;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }


    public void updateFromDTO(BoardDto boardDTO){
        this.title = boardDTO.getTitle();
        this.contents = boardDTO.getContents();
    }


}
