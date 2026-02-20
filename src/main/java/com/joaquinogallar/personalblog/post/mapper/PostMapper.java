package com.joaquinogallar.personalblog.post.mapper;

import com.joaquinogallar.personalblog.comment.mapper.CommentMapper;
import com.joaquinogallar.personalblog.post.dto.PostResponse;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.tag.mapper.TagMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;

    public PostMapper(TagMapper tagMapper, CommentMapper commentMapper) {
        this.tagMapper = tagMapper;
        this.commentMapper = commentMapper;
    }

    public PostResponse mapPostToDto(Post post) {
        if(post == null) return null;

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getSlug(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getPublished(),
                post.getAuthor().getId(),
                new HashSet<>(tagMapper.mapToTagsDto(new ArrayList<>(post.getTags())))
        );
    }

    public List<PostResponse> mapPostsToDto(List<Post> posts) {
        if(posts == null) return null;
        return posts.stream().map(this::mapPostToDto).collect(Collectors.toList());
    }

    public Page<PostResponse> mapPostsToDto(Page<Post> posts) {
        if(posts == null) return null;
        return posts.map(this::mapPostToDto);
    }
}
