package com.joaquinogallar.personalblog.tag.service;

import com.joaquinogallar.personalblog.tag.dto.TagDto;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.tag.mapper.TagMapper;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService implements ITagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    // GET
    @Override
    public List<TagDto> getAllTags() {
        return tagMapper.mapToTagDto(tagRepository.findAll());
    }

    @Override
    public TagDto getTagByName(String name) {
        return tagMapper.mapToTagDto(tagRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Tag " + name + " not found")));
    }

    @Override
    public TagDto getTagById(Long id) {
        return tagMapper.mapToTagDto(tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag " + id + " not found")));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // POST
    @Override
    public TagDto createTag(TagDto tagDto) {
        Tag tag = Tag.builder()
                .name(tagDto.name())
                .slug(tagDto.slug())
                .build();

        tagRepository.save(tag);

        return tagDto;
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    public TagDto updateTag(Long id, TagDto tagDto) {
        Tag tag =  tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag " + id + " not found"));

        tag.setName(tagDto.name());
        tag.setSlug(tagDto.slug());

        tagRepository.save(tag);

        return tagDto;
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    public TagDto deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag " + id + " not found"));

        tagRepository.delete(tag);

        return tagMapper.mapToTagDto(tag);
    }
}
