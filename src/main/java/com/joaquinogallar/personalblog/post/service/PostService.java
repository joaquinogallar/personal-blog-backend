package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.CreatePostRequest;
import com.joaquinogallar.personalblog.post.dto.PostResponse;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.post.mapper.PostMapper;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
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

    public PostService(PostRepository postRepository, TagRepository tagRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
    }

    // GET
    @Override
    public List<PostResponse> getAllPosts() {
        return postMapper.mapPostsToDto(postRepository.findAll());
    }

    @Override
    public PostResponse getPostByTitle(String title) {
        return postMapper.mapPostToDto(postRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Not found: " + title)));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // POST
    @Override
    @Transactional
    public PostResponse createPost(CreatePostRequest postReq) {

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
                .build();

        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    @Transactional
    public PostResponse updatePost(Long idPost, CreatePostRequest postReq) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new EntityNotFoundException("Post " + idPost + " not found"));

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
