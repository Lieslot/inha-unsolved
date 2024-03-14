package com.project.inhaUnsolved.domain.problem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.inhaUnsolved.domain.problem.domain.Tag;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TagDetail {

    private int bojTagId;
    @JsonProperty("displayNames")
    private List<TagName> tagNames;

    public TagDetail() {
        tagNames = new ArrayList<>();
    }

    public Tag toTag() {
        return Tag.builder()
                  .name(String.valueOf(tagNames.stream()
                                               .filter(TagName::isKoreanName)
                                               .findFirst()
                                               .orElse(new TagName())
                                               .getName()))
                  .number(bojTagId)
                  .build();
    }
}
