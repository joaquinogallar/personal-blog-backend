package com.joaquinogallar.personalblog.post.controller;

import com.joaquinogallar.personalblog.post.dto.CreatePostRequest;
import com.joaquinogallar.personalblog.post.dto.PostResponse;
import com.joaquinogallar.personalblog.post.service.IPostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final IPostService postService;

    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{postTitle}")
    public ResponseEntity<PostResponse> getPostByTitle(@PathVariable String postTitle) {
        return ResponseEntity.ok(postService.getPostByTitle(postTitle));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest postReq) {
        return ResponseEntity.ok(postService.createPost(postReq, "testUser")); // hadrcoded username for testing
    }

    @PostMapping("/{idPost}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long idPost, @RequestBody CreatePostRequest postReq) {
        return ResponseEntity.ok(postService.updatePost(idPost, postReq));
    }

    @DeleteMapping("/{idPost}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long idPost) {
        return ResponseEntity.ok(postService.deletePost(idPost));
    }
}
