package com.joaquinogallar.personalblog.tag.mapper;

import com.joaquinogallar.personalblog.tag.dto.TagResponse;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public TagResponse mapToTagDto(Tag tag) {
        if(tag == null) return null;

        return new TagResponse(
                tag.getId(),
                tag.getName(),
                tag.getSlug()
        );
    }

    public List<TagResponse> mapToTagDto(List<Tag> tags) {
        if(tags == null) return null;
        return tags.stream().map(this::mapToTagDto).collect(Collectors.toList());
    }
}
