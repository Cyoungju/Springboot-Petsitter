package com.example.petsitter.board.service;

import com.example.petsitter.board.domain.Board;
import com.example.petsitter.board.dto.BoardDto;
import com.example.petsitter.board.repository.BoardRepository;
import com.example.petsitter.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public Page<BoardDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // 페이지가 1부터 시작하기 (페이지 시작 번호 셋팅)
        int size = 5; // 몇개의 게시물을 넣을것인가 (페이지에 포함될 개수)

        //전체 게시물 불러오기
        Page<Board> boards = boardRepository.findAll(
                // 정렬시켜서 사가져옴
                PageRequest.of(page, size)
        );

        return boards.map(board -> new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime(),
                board.getMember(),
                board.getMember().getEmail()
        ));
    }

    @Override
    public BoardDto findById(Long id) {
        Optional<Board> boardOptional = boardRepository.findById(id);

        if(boardOptional.isPresent()){
            Board board = boardRepository.findById(id).get();

            return BoardDto.toboardDTO(board);
        }else {
            return null;
        }
    }

    @Override
    public void save(BoardDto boardDTO, Member member) {
        boardDTO.setCreateTime(LocalDateTime.now());
        boardRepository.save(boardDTO.toEntity(member));
    }

    @Override
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    public void update(BoardDto boardDTO) {
        Optional<Board> boardOptional = boardRepository.findById(boardDTO.getId());

        if(boardOptional.isPresent()){
            Board board = boardOptional.get();
            board.updateFromDTO(boardDTO);

            boardRepository.save(board);
        }
    }
}
