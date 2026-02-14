package com.joaquinogallar.personalblog.tag.service;

import com.joaquinogallar.personalblog.tag.dto.CreateTagRequest;
import com.joaquinogallar.personalblog.tag.dto.TagResponse;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.tag.mapper.TagMapper;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<TagResponse> getAllTags() {
        return tagMapper.mapToTagDto(tagRepository.findAll());
    }

    @Override
    public TagResponse getTagByName(String name) {
        return tagMapper.mapToTagDto(tagRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Tag " + name + " not found")));
    }

    @Override
    public TagResponse getTagById(Long id) {
        return tagMapper.mapToTagDto(tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag " + id + " not found")));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // POST
    @Override
    @Transactional
    public TagResponse createTag(CreateTagRequest tagReq) {
        Tag tag = Tag.builder()
                .name(tagReq.name())
                .slug(tagReq.slug())
                .build();

        tagRepository.save(tag);

        return tagMapper.mapToTagDto(tag);
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    @Transactional
    public TagResponse updateTag(Long id, CreateTagRequest tagReq) {
        Tag tag =  tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag " + id + " not found"));

        tag.setName(tagReq.name());
        tag.setSlug(tagReq.slug());

        tagRepository.save(tag);

        return tagMapper.mapToTagDto(tag);
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    @Transactional
    public TagResponse deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag " + id + " not found"));

        tagRepository.delete(tag);

        return tagMapper.mapToTagDto(tag);
    }
}
