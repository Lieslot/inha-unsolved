package com.project.inhaUnsolved.domain.problem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TagDetail {

    private int bojTagId;
    private List<TagName> tagNames;

    public TagDetail() {
        tagNames = new ArrayList<>();
    }
}
