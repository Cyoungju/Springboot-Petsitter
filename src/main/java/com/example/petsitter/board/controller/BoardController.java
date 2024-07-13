package com.example.petsitter.board.controller;

import com.example.petsitter.board.dto.BoardDto;
import com.example.petsitter.board.dto.CommentDto;
import com.example.petsitter.board.service.BoardService;
import com.example.petsitter.board.service.CommentService;
import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("boardDto", new BoardDto());
        return "board/create";
    }

    @PostMapping("/")
    public String register(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid BoardDto boardDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "board/create";
        }
        Member member = memberService.findByEmail(customUserDetails.getUsername());
        boardService.save(boardDto, member);
        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDto> boards = boardService.paging(pageable);

        int blockLimit = 3;

        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;

        int endPage = ((startPage + blockLimit - 1) < boards.getTotalPages()) ? (startPage + blockLimit - 1) : boards.getTotalPages();

        model.addAttribute("boardList", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/list";
    }


    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("boardDto", boardDto);

        return "board/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDto boardDto){
        boardService.update(boardDto);
        return "redirect:/board/list";
    }

    @GetMapping("/{id}")
    public String paging(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable){
        //게시글 정보 가져오기
        BoardDto dto = boardService.findById(id);
        Member member = dto.getMember();

        //댓글 정보 가져오기
        List<CommentDto> commentList = commentService.findAll(id);


        //모델에 데이터 추가
        model.addAttribute("member", member);
        model.addAttribute("board", dto);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("commentList", commentList);

        return "board/detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/list";
    }

}
