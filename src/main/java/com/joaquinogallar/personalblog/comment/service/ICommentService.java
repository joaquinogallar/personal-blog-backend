package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.dto.CommentResponse;
import com.joaquinogallar.personalblog.comment.dto.CreateCommentRequest;
import com.joaquinogallar.personalblog.security.entity.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    Page<CommentResponse> getAllCommentsInPost(Long postId, Pageable pageable);
    String comment(CreateCommentRequest comment, Long idPost, CustomUserDetails userDetails);
    String deleteComment(Long commentId);
}
