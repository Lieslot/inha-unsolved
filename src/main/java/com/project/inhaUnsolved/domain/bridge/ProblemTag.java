package com.project.inhaUnsolved.domain.bridge;

import com.project.inhaUnsolved.domain.problem.Problem;
import com.project.inhaUnsolved.domain.problem.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.hibernate.annotations.Fetch;

@Getter
@Entity
public class ProblemTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    private Problem problem;


}
