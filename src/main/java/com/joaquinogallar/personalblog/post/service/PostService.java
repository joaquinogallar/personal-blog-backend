package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.PostDto;
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
    public List<PostDto> getAllPosts() {
        return postMapper.mapPostsToDto(postRepository.findAll());
    }

    @Override
    public PostDto getPostByTitle(String title) {
        return postMapper.mapPostToDto(postRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Not found: " + title)));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // POST
    @Override
    public PostDto createPost(PostDto postDto) {

        Set<Tag> tags = postDto.tags()
                .stream()
                .map(t -> tagRepository.findById(t.id())
                        .orElseThrow(() -> new RuntimeException("tag not found " + t.id())))
                .collect(Collectors.toSet());

        Post post = Post.builder()
                .title(postDto.title())
                .content(postDto.content())
                .slug(postDto.slug())
                .tags(tags)
                .build();

        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    public PostDto updatePost(Long idPost, PostDto postDto) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new EntityNotFoundException("Not found: " + idPost));

        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setSlug(postDto.slug());

        return postMapper.mapPostToDto(postRepository.save(post));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    public PostDto deletePost(Long idPost) {
        return null;
    }
}
