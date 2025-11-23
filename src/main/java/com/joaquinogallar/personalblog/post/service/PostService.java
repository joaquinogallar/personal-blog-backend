package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.PostResponse;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.post.mapper.PostMapper;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

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
    public PostResponse createPost(PostResponse postResponse) {

        Set<Tag> tags = postResponse.tags()
                .stream()
                .map(t -> tagRepository.findById(t.id())
                        .orElseThrow(() -> new RuntimeException("Tag " + t.id() + " not found")))
                .collect(Collectors.toSet());

        Post post = Post.builder()
                .title(postResponse.title())
                .content(postResponse.content())
                .slug(postResponse.slug())
                .tags(tags)
                .build();

        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    public PostResponse updatePost(Long idPost, PostResponse postResponse) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new EntityNotFoundException("Post " + idPost + " not found"));

        post.setTitle(postResponse.title());
        post.setContent(postResponse.content());
        post.setSlug(postResponse.slug());

        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    public PostResponse deletePost(Long idPost) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new EntityNotFoundException("Post " + idPost + " not found"));

        postRepository.delete(post);

        return postMapper.mapPostToDto(post);
    }
}
