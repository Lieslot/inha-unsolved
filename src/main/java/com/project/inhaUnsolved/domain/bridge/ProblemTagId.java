package com.project.inhaUnsolved.domain.bridge;

import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class ProblemTagId implements Serializable {

    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "problem_id")
    private Integer problemId;

}
