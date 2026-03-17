package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.CreatePostRequest;
import com.joaquinogallar.personalblog.post.dto.PostResponse;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.post.exception.PostNotFoundException;
import com.joaquinogallar.personalblog.post.mapper.PostMapper;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private User author;
    private Tag tag;
    private Post post;
    private PostResponse postResponse;
    private CreatePostRequest createPostRequest;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .id(UUID.randomUUID())
                .username("joaquin")
                .email("joaquin@example.com")
                .roles(Set.of(Role.ADMIN))
                .build();

        tag = Tag.builder()
                .id(1L)
                .name("Spring Boot")
                .slug("spring-boot")
                .build();

        post = Post.builder()
                .id(1L)
                .title("My first post")
                .content("Hello world")
                .slug("my-first-post")
                .published(false)
                .author(author)
                .tags(Set.of(tag))
                .build();

        postResponse = new PostResponse(
                1L,
                "My first post",
                "Hello world",
                "my-first-post",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                author.getId(),
                Set.of(),
                1
        );

        createPostRequest = new CreatePostRequest(
                "My first post",
                "Hello world",
                "my-first-post",
                true,
                Set.of(1L)
        );
    }

    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("getAllPosts")
    class GetAllPosts {

        @Test
        @DisplayName("should return a paginated list of posts")
        void shouldReturnPaginatedPosts() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Post> postPage = new PageImpl<>(List.of(post));
            Page<PostResponse> responsePage = new PageImpl<>(List.of(postResponse));

            given(postRepository.findAll(pageable)).willReturn(postPage);
            given(postMapper.mapPostsToDto(postPage)).willReturn(responsePage);

            Page<PostResponse> result = postService.getAllPosts(pageable);

            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).title()).isEqualTo("My first post");
        }
    }

    @Nested
    @DisplayName("getPostByTitle")
    class GetPostByTitle {

        @Test
        @DisplayName("should return post when title exists")
        void shouldReturnPostWhenTitleExists() {
            given(postRepository.findByTitle("My first post")).willReturn(Optional.of(post));
            given(postMapper.mapPostToDto(post)).willReturn(postResponse);

            PostResponse result = postService.getPostByTitle("My first post");

            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo("My first post");
        }

        @Test
        @DisplayName("should throw PostNotFoundException when title does not exist")
        void shouldThrowExceptionWhenTitleNotFound() {
            given(postRepository.findByTitle("nonexistent")).willReturn(Optional.empty());

            assertThatThrownBy(() -> postService.getPostByTitle("nonexistent"))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessageContaining("nonexistent");
        }
    }

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("createPost")
    class CreatePost {

        @Test
        @DisplayName("should create and return the new post")
        void shouldCreateAndReturnPost() {
            given(postRepository.existsByTitle("My first post")).willReturn(false);
            given(postRepository.existsBySlug("my-first-post")).willReturn(false);
            given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
            given(userRepository.findUserByUsername("joaquin")).willReturn(Optional.of(author));
            given(postRepository.save(any(Post.class))).willReturn(post);
            given(postMapper.mapPostToDto(post)).willReturn(postResponse);

            PostResponse result = postService.createPost(createPostRequest, "joaquin");

            verify(postRepository).save(any(Post.class));
            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo("My first post");
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when title is already taken")
        void shouldThrowWhenTitleAlreadyExists() {
            given(postRepository.existsByTitle("My first post")).willReturn(true);

            assertThatThrownBy(() -> postService.createPost(createPostRequest, "joaquin"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("title is already in use");

            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when slug is already taken")
        void shouldThrowWhenSlugAlreadyExists() {
            given(postRepository.existsByTitle("My first post")).willReturn(false);
            given(postRepository.existsBySlug("my-first-post")).willReturn(true);

            assertThatThrownBy(() -> postService.createPost(createPostRequest, "joaquin"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("slug is already in use");

            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw UserNotFoundException when author username does not exist")
        void shouldThrowWhenAuthorNotFound() {
            given(postRepository.existsByTitle("My first post")).willReturn(false);
            given(postRepository.existsBySlug("my-first-post")).willReturn(false);
            given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
            given(userRepository.findUserByUsername("ghost")).willReturn(Optional.empty());

            assertThatThrownBy(() -> postService.createPost(createPostRequest, "ghost"))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("not found");

            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("should ignore non-existent tag ids and not fail")
        void shouldIgnoreNonExistentTagIds() {
            CreatePostRequest requestWithInvalidTag = new CreatePostRequest(
                    "My first post", "Hello world", "my-first-post", true, Set.of(999L)
            );

            given(postRepository.existsByTitle("My first post")).willReturn(false);
            given(postRepository.existsBySlug("my-first-post")).willReturn(false);
            given(tagRepository.findById(999L)).willReturn(Optional.empty()); // returns null via orElse(null)
            given(userRepository.findUserByUsername("joaquin")).willReturn(Optional.of(author));
            given(postRepository.save(any(Post.class))).willReturn(post);
            given(postMapper.mapPostToDto(post)).willReturn(postResponse);

            PostResponse result = postService.createPost(requestWithInvalidTag, "joaquin");

            assertThat(result).isNotNull();
            verify(postRepository).save(any(Post.class));
        }
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("updatePost")
    class UpdatePost {

        @Test
        @DisplayName("should update and return the modified post")
        void shouldUpdateAndReturnPost() {
            CreatePostRequest updateRequest = new CreatePostRequest(
                    "Updated title", "Updated content", "updated-slug", true, Set.of()
            );
            PostResponse updatedResponse = new PostResponse(
                    1L, "Updated title", "Updated content", "updated-slug",
                    LocalDateTime.now(), LocalDateTime.now(), false, author.getId(), Set.of(), 1
            );

            given(postRepository.findById(1L)).willReturn(Optional.of(post));
            given(postRepository.existsByTitleAndIdNot("Updated title", 1L)).willReturn(false);
            given(postRepository.existsBySlugAndIdNot("updated-slug", 1L)).willReturn(false);
            given(postRepository.save(post)).willReturn(post);
            given(postMapper.mapPostToDto(post)).willReturn(updatedResponse);

            PostResponse result = postService.updatePost(1L, updateRequest);

            verify(postRepository).save(post);
            assertThat(result.title()).isEqualTo("Updated title");
        }

        @Test
        @DisplayName("should throw PostNotFoundException when post to update does not exist")
        void shouldThrowWhenPostToUpdateNotFound() {
            given(postRepository.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> postService.updatePost(99L, createPostRequest))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessageContaining("99");

            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when new title belongs to another post")
        void shouldThrowWhenTitleConflictsWithAnotherPost() {
            given(postRepository.findById(1L)).willReturn(Optional.of(post));
            given(postRepository.existsByTitleAndIdNot("My first post", 1L)).willReturn(true);

            assertThatThrownBy(() -> postService.updatePost(1L, createPostRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("title is already in use");
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when new slug belongs to another post")
        void shouldThrowWhenSlugConflictsWithAnotherPost() {
            given(postRepository.findById(1L)).willReturn(Optional.of(post));
            given(postRepository.existsByTitleAndIdNot("My first post", 1L)).willReturn(false);
            given(postRepository.existsBySlugAndIdNot("my-first-post", 1L)).willReturn(true);

            assertThatThrownBy(() -> postService.updatePost(1L, createPostRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("slug is already in use");
        }

        @Test
        @DisplayName("should apply new title, content and slug to the entity")
        void shouldApplyNewFieldsToPost() {
            CreatePostRequest updateRequest = new CreatePostRequest(
                    "New title", "New content", "new-slug", true, Set.of()
            );

            given(postRepository.findById(1L)).willReturn(Optional.of(post));
            given(postRepository.existsByTitleAndIdNot("New title", 1L)).willReturn(false);
            given(postRepository.existsBySlugAndIdNot("new-slug", 1L)).willReturn(false);
            given(postRepository.save(post)).willReturn(post);
            given(postMapper.mapPostToDto(post)).willReturn(postResponse);

            postService.updatePost(1L, updateRequest);

            assertThat(post.getTitle()).isEqualTo("New title");
            assertThat(post.getContent()).isEqualTo("New content");
            assertThat(post.getSlug()).isEqualTo("new-slug");
        }
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("deletePost")
    class DeletePost {

        @Test
        @DisplayName("should delete post and return its data")
        void shouldDeletePostAndReturnData() {
            given(postRepository.findById(1L)).willReturn(Optional.of(post));
            given(postMapper.mapPostToDto(post)).willReturn(postResponse);

            PostResponse result = postService.deletePost(1L);

            verify(postRepository).delete(post);
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw PostNotFoundException when post to delete does not exist")
        void shouldThrowWhenPostToDeleteNotFound() {
            given(postRepository.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> postService.deletePost(99L))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessageContaining("99");

            verify(postRepository, never()).delete(any());
        }
    }
}