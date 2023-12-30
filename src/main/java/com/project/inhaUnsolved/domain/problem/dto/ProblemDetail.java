package com.project.inhaUnsolved.domain.problem.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.parsing.Problem;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProblemDetail {

    private int problemId;
    private String titleKo;
    private boolean isSolvable;
    private int level;

    public UnsolvedProblem toUnsolvedProblem() {
        return UnsolvedProblem.builder()
                .name(titleKo)
                .number(problemId)
                .tier(Tier.valueOf(level))
                .build();
    }

}
