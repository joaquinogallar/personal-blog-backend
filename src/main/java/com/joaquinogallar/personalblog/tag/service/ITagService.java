package com.joaquinogallar.personalblog.tag.service;

import com.joaquinogallar.personalblog.tag.dto.TagDto;

import java.util.List;

public interface ITagService {
    List<TagDto> getAllTags();
    TagDto getTagByName(String name);
    TagDto getTagById(Long id);
    TagDto createTag(TagDto tagDto);
    TagDto updateTag(Long id, TagDto tagDto);
    TagDto deleteTag(Long id);
}
