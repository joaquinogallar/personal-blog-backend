package com.joaquinogallar.personalblog.comment.service;

import com.joaquinogallar.personalblog.comment.dto.CreateCommentRequest;
import com.joaquinogallar.personalblog.comment.entity.Comment;
import com.joaquinogallar.personalblog.comment.exception.CommentNotFoundException;
import com.joaquinogallar.personalblog.comment.mapper.CommentMapper;
import com.joaquinogallar.personalblog.comment.repository.CommentRepository;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.post.exception.PostNotFoundException;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import com.joaquinogallar.personalblog.user.entity.Role;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.exception.UserNotFoundException;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService")
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private UUID userId;
    private User user;
    private Post post;
    private Comment comment;
    private CreateCommentRequest createCommentRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .username("joaquin")
                .email("joaquin@example.com")
                .roles(Set.of(Role.USER))
                .build();

        post = Post.builder()
                .id(1L)
                .title("My first post")
                .content("Hello world")
                .slug("my-first-post")
                .comments(new ArrayList<>()) // mutable list — required by service logic
                .build();

        comment = Comment.builder()
                .id(1L)
                .content("Great post!")
                .authorName("joaquin")
                .authorEmail("joaquin@example.com")
                .user(user)
                .post(post)
                .build();

        // NOTE: authorName and authorEmail are set from the User entity when userId is provided.
        // These fields in the request are only used for anonymous comments (future feature).
        createCommentRequest = new CreateCommentRequest("Great post!", null);
    }

    // -------------------------------------------------------------------------
    // COMMENT
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("comment")
    class CommentAction {

        @Test
        @DisplayName("should create comment and return success message")
        void shouldCreateCommentSuccessfully() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(postRepository.findById(1L)).willReturn(Optional.of(post));

            String result = commentService.comment(createCommentRequest, 1L, null);

            verify(postRepository).save(post);
            assertThat(result).isEqualTo("Comment sent");
        }

        @Test
        @DisplayName("should add comment to the post's comment list")
        void shouldAddCommentToPost() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(postRepository.findById(1L)).willReturn(Optional.of(post));

            commentService.comment(createCommentRequest, 1L, null);

            assertThat(post.getComments()).hasSize(1);
            assertThat(post.getComments().get(0).getContent()).isEqualTo("Great post!");
        }

        @Test
        @DisplayName("should set authorName and authorEmail from the authenticated user")
        void shouldSetAuthorFieldsFromUser() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(postRepository.findById(1L)).willReturn(Optional.of(post));

            commentService.comment(createCommentRequest, 1L, null);

            Comment saved = post.getComments().get(0);
            assertThat(saved.getAuthorName()).isEqualTo("joaquin");
            assertThat(saved.getAuthorEmail()).isEqualTo("joaquin@example.com");
        }

        @Test
        @DisplayName("should throw UserNotFoundException when userId does not exist")
        void shouldThrowWhenUserNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(userRepository.findById(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> commentService.comment(createCommentRequest, 1L, null))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(unknownId.toString());

            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw PostNotFoundException when postId does not exist")
        void shouldThrowWhenPostNotFound() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(postRepository.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> commentService.comment(createCommentRequest, 99L, null))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessageContaining("99");

            verify(postRepository, never()).save(any());
        }
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("deleteComment")
    class DeleteComment {

        @Test
        @DisplayName("should delete comment and return success message")
        void shouldDeleteCommentSuccessfully() {
            given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

            String result = commentService.deleteComment(1L);

            verify(commentRepository).delete(comment);
            assertThat(result).isEqualTo("Comment deleted successfully");
        }

        @Test
        @DisplayName("should throw CommentNotFoundException when comment does not exist")
        void shouldThrowWhenCommentNotFound() {
            given(commentRepository.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> commentService.deleteComment(99L))
                    .isInstanceOf(CommentNotFoundException.class)
                    .hasMessageContaining("99");

            verify(commentRepository, never()).delete(any());
        }
    }
}