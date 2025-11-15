package com.joaquinogallar.personalblog.post.service;

import com.joaquinogallar.personalblog.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService implements IPostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

}
