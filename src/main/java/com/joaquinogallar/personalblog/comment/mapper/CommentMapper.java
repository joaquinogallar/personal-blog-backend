package com.joaquinogallar.personalblog.comment.mapper;

import com.joaquinogallar.personalblog.comment.dto.CommentDto;
import com.joaquinogallar.personalblog.comment.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDto mapToCommentDto(Comment comment) {
        if (comment == null) return null;

        return new CommentDto(
                comment.getId(),
                comment.getAuthorName(),
                comment.getAuthorEmail(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getPost().getId(),
                comment.getUser().getId()
        );
    }

    public List<CommentDto> mapToCommentDtoList(List<Comment> comments) {
        if (comments == null) return null;
        return comments.stream().map(this::mapToCommentDto).collect(Collectors.toList());
    }

}
