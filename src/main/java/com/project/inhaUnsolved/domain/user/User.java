package com.project.inhaUnsolved.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String handle;

    @Column(nullable = false)
    private int solvingProblemCount;

    @Builder
    public User(String handle, int solvingProblemCount) {
        this.handle = handle;
        this.solvingProblemCount = solvingProblemCount;
    }

    public void renewSolvedCount(int solvedCount) {

        solvingProblemCount = solvedCount;

    }

    @Override
    public String toString() {
        return "{" + handle + ", " +
                String.valueOf(solvingProblemCount) + "}";
    }
}
