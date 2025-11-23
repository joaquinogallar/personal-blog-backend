package com.joaquinogallar.personalblog.tag.service;

import com.joaquinogallar.personalblog.tag.dto.TagResponse;

import java.util.List;

public interface ITagService {
    List<TagResponse> getAllTags();
    TagResponse getTagByName(String name);
    TagResponse getTagById(Long id);
    TagResponse createTag(TagResponse tagResponse);
    TagResponse updateTag(Long id, TagResponse tagResponse);
    TagResponse deleteTag(Long id);
}
