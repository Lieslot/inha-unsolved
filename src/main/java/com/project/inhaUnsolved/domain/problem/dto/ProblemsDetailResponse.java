package com.project.inhaUnsolved.domain.problem.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemsDetailResponse {


    private int count;
    private List<ProblemDetail> items;

    public ProblemsDetailResponse() {
        items = new ArrayList<>();
    }

    public ProblemsDetailResponse(int count) {
        this.count = count;
        items = new ArrayList<>();
    }

    public ProblemsDetailResponse(int count, List<ProblemDetail> items) {
        this.count = count;
        items = new ArrayList<>(items);
    }
}
