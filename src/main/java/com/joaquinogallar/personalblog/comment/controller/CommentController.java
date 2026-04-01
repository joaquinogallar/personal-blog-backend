package com.joaquinogallar.personalblog.comment.controller;

import com.joaquinogallar.personalblog.comment.dto.CommentResponse;
import com.joaquinogallar.personalblog.comment.dto.CreateCommentRequest;
import com.joaquinogallar.personalblog.comment.service.ICommentService;
import com.joaquinogallar.personalblog.security.entity.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsInPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getAllCommentsInPost(postId));
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> comment(@PathVariable Long postId,
                                          @RequestBody CreateCommentRequest commentReq,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null) {
            if(commentReq.authorEmail() == null || commentReq.authorEmail().isBlank()) {
                return ResponseEntity.badRequest().body("The email is required if you're not logged in.");
            }
        }
        return ResponseEntity
                .status(201)
                .body(commentService.comment(commentReq, postId, userDetails));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity
                .status(204)
                .body(commentService.deleteComment(commentId));
    }
}
