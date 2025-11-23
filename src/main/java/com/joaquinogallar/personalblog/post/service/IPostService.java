package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.PostResponse;

import java.util.List;

public interface IPostService {
    List<PostResponse> getAllPosts();
    PostResponse getPostByTitle(String title);
    PostResponse createPost(PostResponse postResponse);
    PostResponse updatePost(Long idPost, PostResponse postResponse);
    PostResponse deletePost(Long idPost);
}
