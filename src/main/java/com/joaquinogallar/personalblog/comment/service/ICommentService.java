package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.dto.CreateCommentRequest;

import java.util.UUID;

public interface ICommentService {
    String comment(CreateCommentRequest comment, Long idPost, UUID userId);
    String deleteComment(Long commentId);
}
