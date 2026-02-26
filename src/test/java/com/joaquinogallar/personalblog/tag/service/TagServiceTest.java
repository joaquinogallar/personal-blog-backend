package com.joaquinogallar.personalblog.tag.service;

import com.joaquinogallar.personalblog.tag.dto.CreateTagRequest;
import com.joaquinogallar.personalblog.tag.dto.TagResponse;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.tag.mapper.TagMapper;
import com.joaquinogallar.personalblog.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TagService")
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagService tagService;

    private Tag tag;
    private TagResponse tagResponse;
    private CreateTagRequest createTagRequest;

    @BeforeEach
    void setUp() {
        tag = Tag.builder()
                .id(1L)
                .name("Spring Boot")
                .slug("spring-boot")
                .build();

        tagResponse = new TagResponse(1L, "Spring Boot", "spring-boot");
        createTagRequest = new CreateTagRequest("Spring Boot", "spring-boot");
    }

    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("getAllTags")
    class GetAllTags {

        @Test
        @DisplayName("should return a paginated list of tags")
        void shouldReturnPaginatedTags() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Tag> tagPage = new PageImpl<>(List.of(tag));
            Page<TagResponse> tagResponsePage = new PageImpl<>(List.of(tagResponse));

            given(tagRepository.findAll(pageable)).willReturn(tagPage);
            given(tagMapper.mapToTagsDto(tagPage)).willReturn(tagResponsePage);

            Page<TagResponse> result = tagService.getAllTags(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).name()).isEqualTo("Spring Boot");
        }

        @Test
        @DisplayName("should return empty page when no tags exist")
        void shouldReturnEmptyPageWhenNoTagsExist() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Tag> emptyPage = Page.empty();

            given(tagRepository.findAll(pageable)).willReturn(emptyPage);
            given(tagMapper.mapToTagsDto(emptyPage)).willReturn(Page.empty());

            Page<TagResponse> result = tagService.getAllTags(pageable);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getTagByName")
    class GetTagByName {

        @Test
        @DisplayName("should return tag when name exists")
        void shouldReturnTagWhenNameExists() {
            given(tagRepository.findByName("Spring Boot")).willReturn(Optional.of(tag));
            given(tagMapper.mapToTagDto(tag)).willReturn(tagResponse);

            TagResponse result = tagService.getTagByName("Spring Boot");

            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("Spring Boot");
            assertThat(result.slug()).isEqualTo("spring-boot");
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when name does not exist")
        void shouldThrowExceptionWhenNameNotFound() {
            given(tagRepository.findByName("nonexistent")).willReturn(Optional.empty());

            assertThatThrownBy(() -> tagService.getTagByName("nonexistent"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("nonexistent");
        }
    }

    @Nested
    @DisplayName("getTagById")
    class GetTagById {

        @Test
        @DisplayName("should return tag when id exists")
        void shouldReturnTagWhenIdExists() {
            given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
            given(tagMapper.mapToTagDto(tag)).willReturn(tagResponse);

            TagResponse result = tagService.getTagById(1L);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when id does not exist")
        void shouldThrowExceptionWhenIdNotFound() {
            given(tagRepository.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> tagService.getTagById(99L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("createTag")
    class CreateTag {

        @Test
        @DisplayName("should create and return the new tag")
        void shouldCreateAndReturnTag() {
            given(tagMapper.mapToTagDto(any(Tag.class))).willReturn(tagResponse);

            TagResponse result = tagService.createTag(createTagRequest);

            verify(tagRepository).save(any(Tag.class));
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("Spring Boot");
            assertThat(result.slug()).isEqualTo("spring-boot");
        }

        @Test
        @DisplayName("should persist a tag with the correct name and slug")
        void shouldPersistTagWithCorrectFields() {
            given(tagMapper.mapToTagDto(any(Tag.class))).willReturn(tagResponse);

            tagService.createTag(createTagRequest);

            verify(tagRepository).save(argThat(savedTag ->
                    savedTag.getName().equals("Spring Boot") &&
                            savedTag.getSlug().equals("spring-boot")
            ));
        }
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("updateTag")
    class UpdateTag {

        @Test
        @DisplayName("should update and return the modified tag")
        void shouldUpdateAndReturnTag() {
            CreateTagRequest updateRequest = new CreateTagRequest("Spring Boot Updated", "spring-boot-updated");
            TagResponse updatedResponse = new TagResponse(1L, "Spring Boot Updated", "spring-boot-updated");

            given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
            given(tagMapper.mapToTagDto(tag)).willReturn(updatedResponse);

            TagResponse result = tagService.updateTag(1L, updateRequest);

            verify(tagRepository).save(tag);
            assertThat(result.name()).isEqualTo("Spring Boot Updated");
            assertThat(result.slug()).isEqualTo("spring-boot-updated");
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when updating a non-existent tag")
        void shouldThrowExceptionWhenTagNotFound() {
            given(tagRepository.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> tagService.updateTag(99L, createTagRequest))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");

            verify(tagRepository, never()).save(any());
        }
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("deleteTag")
    class DeleteTag {

        @Test
        @DisplayName("should delete the tag and return its data")
        void shouldDeleteTagAndReturnData() {
            given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
            given(tagMapper.mapToTagDto(tag)).willReturn(tagResponse);

            TagResponse result = tagService.deleteTag(1L);

            verify(tagRepository).delete(tag);
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when deleting a non-existent tag")
        void shouldThrowExceptionWhenDeletingNonExistentTag() {
            given(tagRepository.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> tagService.deleteTag(99L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");

            verify(tagRepository, never()).delete(any());
        }
    }
}