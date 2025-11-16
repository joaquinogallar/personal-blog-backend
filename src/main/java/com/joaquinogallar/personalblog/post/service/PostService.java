package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.PostDto;
import com.joaquinogallar.personalblog.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements IPostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDto> getAllPosts() {
        return List.of();
    }

    @Override
    public PostDto getPostByTitle(String title) {
        return null;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        return null;
    }

    @Override
    public PostDto updatePost(Long idPost, PostDto postDto) {
        return null;
    }

    @Override
    public PostDto deletePost(Long idPost) {
        return null;
    }
}
