package com.project.inhaUnsolved.domain.bridge;

import com.project.inhaUnsolved.domain.problem.Problem;
import com.project.inhaUnsolved.domain.problem.Tag;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Fetch;

public class ProblemTag {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    private Problem problem;


}
