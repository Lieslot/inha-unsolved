package com.project.inhaUnsolved.domain.problem;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Problem {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private int number;

    private String name;

    @Column(nullable = false)
    private Tier tier;

}
