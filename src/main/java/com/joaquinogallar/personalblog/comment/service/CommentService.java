package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.repository.CommentRepository;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public String comment(String comment, Long idPost) {
        return "";
    }

    @Override
    public String deleteComment(Long commentId) {
        return "";
    }
}
