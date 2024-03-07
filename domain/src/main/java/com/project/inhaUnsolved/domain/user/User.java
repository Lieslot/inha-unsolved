package com.project.inhaUnsolved.domain.user;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;

        return this.handle.equals(other.handle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.handle);
    }

    @Override
    public String toString() {
        return "{" + handle + ", " +
                solvingProblemCount + "}";
    }

    public boolean hasEqualSolvingCount(User other) {
        return this.solvingProblemCount == other.solvingProblemCount;
    }
}
