package com.joaquinogallar.personalblog.tag.controller;

import com.joaquinogallar.personalblog.tag.dto.CreateTagRequest;
import com.joaquinogallar.personalblog.tag.dto.TagResponse;
import com.joaquinogallar.personalblog.tag.service.ITagService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<Page<TagResponse>> getAllTags(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(tagService.getAllTags(pageable));
    }

    @GetMapping("/name/{tagName}")
    public ResponseEntity<TagResponse> getTagByName(@PathVariable String tagName) {
        return ResponseEntity.ok(tagService.getTagByName(tagName));
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable Long tagId) {
        return ResponseEntity.ok(tagService.getTagById(tagId));
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody @Valid CreateTagRequest tagReq) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagService.createTag(tagReq));
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Long tagId, @RequestBody @Valid CreateTagRequest tagReq) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagService.updateTag(tagId, tagReq));
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<TagResponse> deleteTag(@PathVariable Long tagId) {
        return ResponseEntity
                .status(204)
                .body(tagService.deleteTag(tagId));
    }

}
