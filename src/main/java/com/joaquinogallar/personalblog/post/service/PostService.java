package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.exception.PostNotFoundException;
import com.joaquinogallar.personalblog.user.exception.UserNotFoundException;
import com.joaquinogallar.personalblog.post.dto.CreatePostRequest;
import com.joaquinogallar.personalblog.post.dto.PostResponse;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.post.mapper.PostMapper;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository, TagRepository tagRepository, PostMapper postMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    public void checkTitleAndSlugAvailability(CreatePostRequest postReq) {
        logger.info("checking title '{}' and slug '{}' availability", postReq.title(), postReq.slug());
        if(postRepository.existsByTitle(postReq.title()))
            throw new IllegalArgumentException("Error: the title is already in use");

        if(postRepository.existsBySlug(postReq.slug()))
            throw new IllegalArgumentException("Error: the slug is already in use");
    }

    public void checkTitleAndSlugAvailability(CreatePostRequest postReq, Long id) {
        logger.info("checking title '{}' and slug '{}' availability for post {}", postReq.title(), postReq.slug(), id);
        if(postRepository.existsByTitleAndIdNot((postReq.title()), id))
            throw new IllegalArgumentException("Error: the title is already in use");

        if(postRepository.existsBySlugAndIdNot(postReq.slug(), id))
            throw new IllegalArgumentException("Error: the slug is already in use");
    }

    // GET
    @Override
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        logger.info("fetching all posts in page: {}", pageable.getPageNumber());
        return postMapper.mapPostsToDto(postRepository.findAll(pageable));
    }

    @Override
    @Transactional(readOnly = true) // because @Lob annotation in content field
    public PostResponse getPostBySlug(String slug) {
        logger.info("searching post with slug '{}'", slug);
        return postMapper.mapPostToDto(postRepository.findBySlug(slug).orElseThrow(() -> new PostNotFoundException("Error: post '%s' doesn't exist".formatted(slug))));
    }

    @Override
    @Transactional(readOnly = true) // because @Lob annotation in content field
    public PostResponse getPostByTitle(String title) {
        logger.info("searching post with title '{}'", title);
        return postMapper.mapPostToDto(postRepository.findByTitle(title).orElseThrow(() -> new PostNotFoundException("Error: post '%s' doesn't exist".formatted(title))));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // POST
    @Override
    @Transactional
    public PostResponse createPost(CreatePostRequest postReq, String username) {
        checkTitleAndSlugAvailability(postReq);

        Set<Tag> tags = postReq.tagIds()
                .stream()
                .map(t -> tagRepository.findById(t)
                        .orElse(null))
                .collect(Collectors.toSet());

        Post post = Post.builder()
                .title(postReq.title())
                .content(postReq.content())
                .slug(postReq.slug())
                .tags(tags)
                .author(userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found")))
                .build();

        logger.info("trying to save a new post");
        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    @Transactional
    public PostResponse updatePost(Long idPost, CreatePostRequest postReq) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new PostNotFoundException("Post " + idPost + " not found"));

        List<Tag> tags = new ArrayList<>();
        tags = tagRepository.findAllById(postReq.tagIds());

        checkTitleAndSlugAvailability(postReq, idPost);

        post.setTitle(postReq.title());
        post.setContent(postReq.content());
        post.setSlug(postReq.slug());
        post.getTags().addAll(tags);

        logger.info("trying to update post {}", idPost);
        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    @Transactional
    public PostResponse deletePost(Long idPost) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new PostNotFoundException("Post " + idPost + " not found"));

        postRepository.delete(post);

        logger.info("trying to delete post {}", idPost);
        return postMapper.mapPostToDto(post);
    }

}
