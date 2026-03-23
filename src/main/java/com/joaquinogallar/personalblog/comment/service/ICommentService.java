package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.dto.CreateCommentRequest;
import com.joaquinogallar.personalblog.security.entity.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface ICommentService {
    String comment(CreateCommentRequest comment, Long idPost, CustomUserDetails userDetails);
    String deleteComment(Long commentId);
}
