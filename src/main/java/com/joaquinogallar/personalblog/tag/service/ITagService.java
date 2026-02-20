package com.joaquinogallar.personalblog.tag.service;

import com.joaquinogallar.personalblog.tag.dto.CreateTagRequest;
import com.joaquinogallar.personalblog.tag.dto.TagResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITagService {
    Page<TagResponse> getAllTags(Pageable pageable);
    TagResponse getTagByName(String name);
    TagResponse getTagById(Long id);
    TagResponse createTag(CreateTagRequest tagResponse);
    TagResponse updateTag(Long id, CreateTagRequest tagResponse);
    TagResponse deleteTag(Long id);
}
