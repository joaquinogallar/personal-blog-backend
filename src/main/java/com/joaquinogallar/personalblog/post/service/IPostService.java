package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.PostDto;

import java.util.List;

public interface IPostService {
    List<PostDto> getAllPosts();
    PostDto getPostByTitle(String title);
    PostDto createPost(PostDto postDto);
    PostDto updatePost(Long idPost, PostDto postDto);
    PostDto deletePost(Long idPost);
}
