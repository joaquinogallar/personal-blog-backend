package com.joaquinogallar.personalblog.post.mapper;

import com.joaquinogallar.personalblog.post.dto.PostDto;
import com.joaquinogallar.personalblog.post.entity.Post;

import java.util.UUID;

public class PostMapper {

    public static PostDto mapPostToDto(Post post) {
        if(post == null) return null;
        UUID userId = post.getAuthor().getId();

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getSlug(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getPublished(),
                userId,
                null,
                null);
    }
}
