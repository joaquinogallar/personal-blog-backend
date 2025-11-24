package com.joaquinogallar.personalblog.tag.service;

import com.joaquinogallar.personalblog.tag.dto.CreateTagRequest;
import com.joaquinogallar.personalblog.tag.dto.TagResponse;

import java.util.List;

public interface ITagService {
    List<TagResponse> getAllTags();
    TagResponse getTagByName(String name);
    TagResponse getTagById(Long id);
    TagResponse createTag(CreateTagRequest tagResponse);
    TagResponse updateTag(Long id, CreateTagRequest tagResponse);
    TagResponse deleteTag(Long id);
}
