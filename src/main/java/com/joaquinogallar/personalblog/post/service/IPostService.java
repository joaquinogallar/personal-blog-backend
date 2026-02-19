package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.dto.CreatePostRequest;
import com.joaquinogallar.personalblog.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostService {
    Page<PostResponse> getAllPosts(Pageable pageable);
    PostResponse getPostByTitle(String title);
    PostResponse createPost(CreatePostRequest postResponse, String username);
    PostResponse updatePost(Long idPost, CreatePostRequest postResponse);
    PostResponse deletePost(Long idPost);
}
