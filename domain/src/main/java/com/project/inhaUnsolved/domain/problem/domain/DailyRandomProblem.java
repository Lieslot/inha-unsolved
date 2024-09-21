package com.project.inhaUnsolved.domain.problem.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class DailyRandomProblem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer problemId;

    @Builder
    public DailyRandomProblem(Integer problemId, Long id) {
        this.problemId = problemId;
        this.id = id;
    }
}
