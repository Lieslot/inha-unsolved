package com.project.inhaUnsolved.domain.problem.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProblemDetail {

    private int problemId;
    private String titleKo;
    @JsonProperty("isSolvable")
    private boolean isSolvable;
    private int level;
    @JsonProperty("tags")
    private List<TagDetail> tagDetails;

    public UnsolvedProblem toUnsolvedProblem() {
        return UnsolvedProblem.builder()
                              .name(titleKo)
                              .number(problemId)
                              .tier(Tier.valueOf(level))
                              .tags(tagDetails.stream()
                                              .map(TagDetail::toTag)
                                              .collect(Collectors.toSet()))
                              .build();
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    @Override
    public String toString() {
        return "{ isSolvable" + isSolvable + "}";
    }
}
