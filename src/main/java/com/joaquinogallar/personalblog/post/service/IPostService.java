package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.CreatePostRequest;
import com.joaquinogallar.personalblog.post.dto.PostResponse;

import java.util.List;

public interface IPostService {
    List<PostResponse> getAllPosts();
    PostResponse getPostByTitle(String title);
    PostResponse createPost(CreatePostRequest postResponse);
    PostResponse updatePost(Long idPost, CreatePostRequest postResponse);
    PostResponse deletePost(Long idPost);
}
