package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.exception.PostNotFoundException;
import com.joaquinogallar.personalblog.post.dto.CreatePostRequest;
import com.joaquinogallar.personalblog.post.dto.PostResponse;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.post.mapper.PostMapper;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository, PostMapper postMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    public void checkTitleAndSlugAvailability(CreatePostRequest postReq) {

        if(postRepository.existsByTitle(postReq.title()))
            throw new IllegalArgumentException("Error: the title is already in use");

        if(postRepository.existsBySlug(postReq.slug()))
            throw new IllegalArgumentException("Error: the slug is already in use");
    }

    public void checkTitleAndSlugAvailability(CreatePostRequest postReq, Long id) {

        if(postRepository.existsByTitleAndIdNot((postReq.title()), id))
            throw new IllegalArgumentException("Error: the title is already in use");

        if(postRepository.existsBySlugAndIdNot(postReq.slug(), id))
            throw new IllegalArgumentException("Error: the slug is already in use");
    }

    // GET
    @Override
    public List<PostResponse> getAllPosts() {
        return postMapper.mapPostsToDto(postRepository.findAll());
    }

    @Override
    public PostResponse getPostByTitle(String title) {
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
                .author(userRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found")))
                .build();

        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    @Transactional
    public PostResponse updatePost(Long idPost, CreatePostRequest postReq) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new EntityNotFoundException("Post " + idPost + " not found"));

        checkTitleAndSlugAvailability(postReq, idPost);

        post.setTitle(postReq.title());
        post.setContent(postReq.content());
        post.setSlug(postReq.slug());

        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    @Transactional
    public PostResponse deletePost(Long idPost) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new EntityNotFoundException("Post " + idPost + " not found"));

        postRepository.delete(post);

        return postMapper.mapPostToDto(post);
    }

}
