package com.project.inhaUnsolved.domain.problem.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDetailResponse {

    private int count;
    private List<TagDetail> items;

    public TagDetailResponse() {
        items = new ArrayList<>();
    }

    public TagDetailResponse(int count) {
        this.count = count;
        items = new ArrayList<>();
    }
}
