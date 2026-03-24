package com.joaquinogallar.personalblog.post.dto;

import com.joaquinogallar.personalblog.tag.dto.TagResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean published;
    private UUID userId;
    private Set<TagResponse> tags;
    private int readTimeInMinutes;
    private boolean isUserAuthenticated;
}