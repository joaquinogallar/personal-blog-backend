package com.joaquinogallar.personalblog.comment.service;

public interface ICommentService {
    String comment(String comment, Long idPost);
    String deleteComment(Long commentId);
}
