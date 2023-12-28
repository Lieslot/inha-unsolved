package com.project.inhaUnsolved.domain.problem;

import com.project.inhaUnsolved.domain.bridge.ProblemTag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

public class Tag {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false, unique = true)
    private int number;


    private String name;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<ProblemTag> problems;

}
