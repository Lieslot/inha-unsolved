package com.project.inhaUnsolved.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String handle;

    private int solvingProblemCount;




}
