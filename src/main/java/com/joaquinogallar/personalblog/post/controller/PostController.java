package com.joaquinogallar.personalblog.post.controller;

import com.joaquinogallar.personalblog.post.dto.CreatePostRequest;
import com.joaquinogallar.personalblog.post.dto.PostResponse;
import com.joaquinogallar.personalblog.post.service.IPostService;
import com.joaquinogallar.personalblog.security.entity.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostResponse> getPostBySlug(@PathVariable String slug,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostResponse post = postService.getPostBySlug(slug);
        post.setUserAuthenticated(userDetails != null);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postTitle}")
    public ResponseEntity<PostResponse> getPostByTitle(@PathVariable String postTitle,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostResponse post = postService.getPostByTitle(postTitle);
        post.setUserAuthenticated(userDetails != null);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest postReq) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.createPost(postReq, auth.getName()));
    }

    @PutMapping("/{idPost}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long idPost, @RequestBody CreatePostRequest postReq) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.updatePost(idPost, postReq));
    }

    @DeleteMapping("/{idPost}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long idPost) {
        return ResponseEntity
                .status(204)
                .body(postService.deletePost(idPost));
    }
}
