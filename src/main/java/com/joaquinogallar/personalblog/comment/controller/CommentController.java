package com.joaquinogallar.personalblog.comment.controller;

import com.joaquinogallar.personalblog.comment.dto.CreateCommentRequest;
import com.joaquinogallar.personalblog.comment.service.ICommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/{userId}")
    public ResponseEntity<String> comment(@PathVariable Long postId,
                                          @PathVariable UUID userId,
                                          @RequestBody CreateCommentRequest commentReq) {
        return ResponseEntity.ok(commentService.comment(commentReq, postId, userId));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}
