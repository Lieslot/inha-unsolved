package com.project.inhaUnsolved.domain.problem.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemDetails {

    List<ProblemDetail> problemDetails;

    public ProblemDetails() {
        this.problemDetails = new ArrayList<>();
    }
}
