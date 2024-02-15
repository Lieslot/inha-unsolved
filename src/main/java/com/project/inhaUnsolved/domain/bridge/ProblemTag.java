package com.project.inhaUnsolved.domain.bridge;

import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ProblemTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private UnsolvedProblem problem;

    @Builder
    ProblemTag(Tag tag, UnsolvedProblem problem) {
        this.problem = problem;
        this.tag = tag;
    }


    public void addProblem(UnsolvedProblem problem) {
        this.problem = problem;
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ProblemTag other = (ProblemTag) obj;

        return this.tag.getNumber() == other.tag.getNumber()
                && this.problem.getNumber() == other.problem.getNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag.getNumber(), problem.getNumber());
    }

    @Override
    public String toString() {
        return "ProblemTag{" +
                "id=" + id +
                ", tag=" + tag.getName() +
                ", tagNumber=" + tag.getNumber() +
                ", problemNumber=" + problem.getNumber() +
                '}';
    }
}


