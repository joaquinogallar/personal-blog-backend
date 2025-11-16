package com.joaquinogallar.personalblog.tag.mapper;

import com.joaquinogallar.personalblog.tag.dto.TagDto;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public TagDto mapToTagDto(Tag tag) {
        if(tag == null) return null;

        return new TagDto(
                tag.getId(),
                tag.getName(),
                tag.getSlug()
        );
    }

    public Set<TagDto> mapToTagDto(Set<Tag> tags) {
        if(tags == null) return null;
        return tags.stream().map(this::mapToTagDto).collect(Collectors.toSet());
    }
}
