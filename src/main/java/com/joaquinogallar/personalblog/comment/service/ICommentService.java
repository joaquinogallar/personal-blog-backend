package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.dto.CommentDto;

public interface ICommentService {
    String comment(CommentDto comment, Long idPost);
    String deleteComment(Long commentId);
}
