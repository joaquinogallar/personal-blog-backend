package com.joaquinogallar.personalblog.post.mapper;

import com.joaquinogallar.personalblog.post.dto.PostDto;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.tag.mapper.TagMapper;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final TagMapper tagMapper;

    public PostMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    public PostDto mapPostToDto(Post post) {
        if(post == null) return null;

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getSlug(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getPublished(),
                post.getAuthor().getId(),
                tagMapper.mapToTagDto(post.getTags()),
                null);
    }
}
