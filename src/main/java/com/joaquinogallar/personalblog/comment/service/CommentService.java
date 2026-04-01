package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.dto.CommentResponse;
import com.joaquinogallar.personalblog.comment.dto.CreateCommentRequest;
import com.joaquinogallar.personalblog.comment.entity.Comment;
import com.joaquinogallar.personalblog.comment.mapper.CommentMapper;
import com.joaquinogallar.personalblog.comment.repository.CommentRepository;
import com.joaquinogallar.personalblog.comment.exception.CommentNotFoundException;
import com.joaquinogallar.personalblog.post.exception.PostNotFoundException;
import com.joaquinogallar.personalblog.security.entity.CustomUserDetails;
import com.joaquinogallar.personalblog.user.exception.UserNotFoundException;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
    public List<CommentResponse> getAllCommentsInPost(Long postId) {
        return List.of();
    }

    @Override
    @Transactional
    public String comment(CreateCommentRequest commentReq, Long postId, CustomUserDetails userDetails) {
        if(userDetails == null)
            if(userRepository.existsByEmail(commentReq.authorEmail())) throw new IllegalArgumentException("The email is already in use");

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post " + postId + " not found"));

        Comment comment = Comment.builder()
                .content(commentReq.content())
                .authorEmail(userDetails != null ? userDetails.getEmail() : commentReq.authorEmail())
                .build();

        post.getComments().add(comment);

        postRepository.save(post);

        return "Comment sent";
    }

    @Override
    @Transactional
    public String deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment " + commentId + " not found"));
        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }
}
