package com.project.inhaUnsolved.domain.problem.dto;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemDetailCompare {


    private UnsolvedProblem existingProblem;
    private UnsolvedProblem newProblemDetail;

    public ProblemDetailCompare(UnsolvedProblem existingProblem, UnsolvedProblem newProblemDetail) {
        this.existingProblem = existingProblem;
        this.newProblemDetail = newProblemDetail;
    }
}
