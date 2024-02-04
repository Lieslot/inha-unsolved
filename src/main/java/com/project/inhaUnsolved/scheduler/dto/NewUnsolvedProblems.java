package com.project.inhaUnsolved.scheduler.dto;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUnsolvedProblems {

    private List<UnsolvedProblem> unsolvedProblems;

    public NewUnsolvedProblems(List<UnsolvedProblem> unsolvedProblems) {
        this.unsolvedProblems = unsolvedProblems;
    }
}
