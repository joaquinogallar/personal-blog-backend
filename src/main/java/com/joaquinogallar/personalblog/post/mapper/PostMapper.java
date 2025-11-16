package com.joaquinogallar.personalblog.post.mapper;

import com.joaquinogallar.personalblog.comment.mapper.CommentMapper;
import com.joaquinogallar.personalblog.post.dto.PostDto;
import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.tag.mapper.TagMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
                commentMapper.mapToCommentDtoList(post.getComments())
        );
    }

    public List<PostDto> mapPostsToDto(List<Post> posts) {
        if(posts == null) return null;
        return posts.stream().map(this::mapPostToDto).collect(Collectors.toList());
    }
}
