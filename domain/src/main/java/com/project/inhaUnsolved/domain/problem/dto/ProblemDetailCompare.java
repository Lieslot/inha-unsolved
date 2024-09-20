package com.project.inhaUnsolved.domain.problem.dto;

import com.project.inhaUnsolved.domain.problem.domain.Problem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemDetailCompare {


    private Problem existingProblem;
    private Problem newProblemDetail;

    public ProblemDetailCompare(Problem existingProblem, Problem newProblemDetail) {
        this.existingProblem = existingProblem;
        this.newProblemDetail = newProblemDetail;
    }
}
