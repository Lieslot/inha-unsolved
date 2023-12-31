package com.project.inhaUnsolved.domain.problem.domain;

import com.project.inhaUnsolved.domain.bridge.ProblemTag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class UnsolvedProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private int number;

    private String name;

    @Column(nullable = false)
    private Tier tier;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<ProblemTag> tags;

    @Builder
    public UnsolvedProblem(int number, String name, Tier tier) {
        this.number = number;
        this.name = name;
        this.tier = tier;

    }

}
