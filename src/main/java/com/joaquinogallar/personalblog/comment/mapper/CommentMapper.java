package com.joaquinogallar.personalblog.comment.mapper;

import com.joaquinogallar.personalblog.comment.dto.CommentResponse;
import com.joaquinogallar.personalblog.comment.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentResponse mapToCommentDto(Comment comment) {
        if (comment == null) return null;

        return new CommentResponse(
                comment.getId(),
                comment.getAuthorName(),
                comment.getAuthorEmail(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getPost().getId(),
                comment.getUser().getId()
        );
    }

    public List<CommentResponse> mapToCommentDtoList(List<Comment> comments) {
        if (comments == null) return null;
        return comments.stream().map(this::mapToCommentDto).collect(Collectors.toList());
    }

}
