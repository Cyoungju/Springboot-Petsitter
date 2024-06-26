package com.example.petsitter.board.service;


import com.example.petsitter.board.dto.BoardDto;
import com.example.petsitter.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BoardService {
    Page<BoardDto> paging(Pageable pageable);

    BoardDto findById(Long id);

    void save(BoardDto boardDTO, Member member);

    void delete(Long id);

    void update(BoardDto boardDTO);
}
