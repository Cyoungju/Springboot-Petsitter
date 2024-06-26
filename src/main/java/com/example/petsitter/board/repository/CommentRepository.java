package com.example.petsitter.board.repository;

import com.example.petsitter.board.domain.Board;
import com.example.petsitter.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardOrderByIdDesc(Board board);

    Optional<Comment> findByIdAndBoard(Long commentId, Board boardEntity);

}
