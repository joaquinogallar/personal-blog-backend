package com.joaquinogallar.personalblog.tag.service;

import com.joaquinogallar.personalblog.tag.dto.TagDto;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService implements ITagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // GET
    @Override
    public List<TagDto> getAllTags() {
        return List.of();
    }

    @Override
    public TagDto getTagByName(String name) {
        return null;
    }

    @Override
    public TagDto getTagById(Long id) {
        return null;
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // POST
    @Override
    public TagDto createTag(TagDto tagDto) {
        return null;
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    public TagDto updateTag(Long id, TagDto tagDto) {
        return null;
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    public TagDto deleteTag(Long id) {
        return null;
    }
}
