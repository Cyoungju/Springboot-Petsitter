package com.example.petsitter.board.service;


import com.example.petsitter.board.domain.Comment;
import com.example.petsitter.board.dto.CommentDto;
import com.example.petsitter.member.domain.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CommentService {
    Comment save(CommentDto commentDTO, Member member);

    List<CommentDto> findAll(Long boardId);

    void update(Long boardId, Long commentId, CommentDto commentDTO);

    CommentDto getCommentById(Long id);

    void delete(Long boardId, Long commentId);

}
