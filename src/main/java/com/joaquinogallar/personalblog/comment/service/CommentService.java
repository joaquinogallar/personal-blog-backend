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
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    @Transactional(readOnly = true) // because @Lob annotation in content field
    public Page<CommentResponse> getAllCommentsInPost(Long postId, Pageable pageable) {
        logger.info("Getting all comments from post {}", postId);
        return commentRepository.findAllByPostId(postId, pageable);
    }

    @Override
    @Transactional
    public String comment(CreateCommentRequest commentReq, Long postId, CustomUserDetails userDetails) {
        if(userDetails == null) {
            if (userRepository.existsByEmail(commentReq.authorEmail()))
                throw new IllegalArgumentException("The email is already in use");

            logger.info("anon user under the email {} is trying to comment in post {}", commentReq.authorEmail(), postId);
        } else
            logger.info("user {} is trying to comment in post {}", userDetails.getUsername(), postId);


        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post " + postId + " not found"));

        boolean isLoggedIn = userDetails != null;

        Comment comment = Comment.builder()
                .content(commentReq.content())
                .authorEmail(isLoggedIn
                        ? userDetails.getEmail()
                        : commentReq.authorEmail())
                .post(post)
                .authorName(isLoggedIn
                        ? userDetails.getUsername()
                        : null)
                .user(isLoggedIn
                        ? userRepository.findUserByEmail(userDetails.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"))
                        : null)
                .build();

        post.getComments().add(comment);

        postRepository.save(post);

        logger.info("comment sent successfully");

        return "Comment sent";
    }

    @Override
    @Transactional
    public String deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment " + commentId + " not found"));
        logger.info("deleting comment {}", commentId);

        commentRepository.delete(comment);

        logger.info("comment deleted successfully");
        return "Comment deleted successfully";
    }
}
