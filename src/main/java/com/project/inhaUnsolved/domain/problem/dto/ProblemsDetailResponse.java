package com.project.inhaUnsolved.domain.problem.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProblemsDetailResponse {


    private int count;
    private List<ProblemDetail> items;

    public ProblemsDetailResponse() {
    }
}
