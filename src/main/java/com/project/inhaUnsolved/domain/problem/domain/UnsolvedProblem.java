package com.project.inhaUnsolved.domain.problem.domain;

import com.project.inhaUnsolved.domain.bridge.ProblemTag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;


@Getter
@Entity
@Table(indexes = {@Index(name = "numberSortIndex", columnList = "number")})
@NoArgsConstructor
public class UnsolvedProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private int id;

    @Column(nullable = false, unique = true)
    private int number;

    private String name;


    @Enumerated(EnumType.ORDINAL)
    private Tier tier;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "problem", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ProblemTag> tags;

    @Builder
    public UnsolvedProblem(int number, String name, Tier tier, Set<Tag> tags) {
        this.number = number;
        this.name = name;
        this.tier = tier;
        this.tags = tags.stream()
                        .map(tag -> ProblemTag.builder()
                                              .problem(this)
                                              .tag(tag)
                                              .build())
                        .collect(Collectors.toSet());

    }

    public boolean hasEqual(int number) {
        return this.number == number;
    }

    public void renewTags(Set<ProblemTag> newProblemTags) {

        newProblemTags.forEach(newProblemTag -> newProblemTag.addProblem(this));

        this.tags.removeIf(existingTag -> !newProblemTags.contains(existingTag));

        this.tags.addAll(newProblemTags);

    }

    public void renewName(String name) {
        this.name = name;
    }

    public void renewTier(Tier tier) {
        this.tier = tier;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UnsolvedProblem other = (UnsolvedProblem) obj;

        return this.name.equals(other.name) && this.tier == other.tier
                && this.tags.equals(other.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.tier, this.tags);
    }

    @Override
    public String toString() {
        return "{ number:" + number + ", name:" + name + ", tag" + this.tags + " }";
    }
}
