package com.example.petsitter.board.controller;


import com.example.petsitter.board.domain.Comment;
import com.example.petsitter.board.dto.CommentDto;
import com.example.petsitter.board.service.CommentService;
import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping("/")
    public ResponseEntity save(@ModelAttribute CommentDto commentDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUsername();
        Member member = memberService.findByEmail(username);

        System.out.println(username);

        Comment comment = commentService.save(commentDTO,member);

        //저장된 댓글가져오기
        List<CommentDto> all = commentService.findAll(commentDTO.getBoardId());

        //예외처리 해주기
        if(comment != null){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }

    }

    /* UPDATE */
    @PutMapping("/update/{id}/comments/{commentId}")
    public ResponseEntity<Long> update(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        commentService.update(id, commentId, commentDto);
        return ResponseEntity.ok(id);
    }

    /* GET COMMENT BY ID */
    @GetMapping("/getComment/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long id) {

        CommentDto comment = commentService.getCommentById(id);

        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}/comments/{commentId}")
    public ResponseEntity<Long> delete(@PathVariable Long id, @PathVariable Long commentId) {
        commentService.delete(id, commentId);
        return ResponseEntity.ok(commentId);
    }

}
