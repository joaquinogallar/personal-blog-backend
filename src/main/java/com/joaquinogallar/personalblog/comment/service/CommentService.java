package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.dto.CommentResponse;
import com.joaquinogallar.personalblog.comment.entity.Comment;
import com.joaquinogallar.personalblog.comment.mapper.CommentMapper;
import com.joaquinogallar.personalblog.comment.repository.CommentRepository;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public String comment(CommentResponse commentResponse, Long idPost) {
        User user = userRepository.findById(commentResponse.userId()).orElseThrow(() -> new EntityNotFoundException("User " + commentResponse.userId() + " not found"));
        Post post = postRepository.findById(idPost).orElseThrow(() -> new EntityNotFoundException("Post " + idPost + " not found"));
        Comment comment = Comment.builder()
                .content(commentResponse.content())
                .user(user)
                .build();

        post.getComments().add(comment);

        postRepository.save(post);

        return "Comment sent";
    }

    @Override
    public String deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Comment " + commentId + " not found"));
        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }
}
