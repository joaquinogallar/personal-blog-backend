package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.dto.CommentResponse;

public interface ICommentService {
    String comment(CommentResponse comment, Long idPost);
    String deleteComment(Long commentId);
}
