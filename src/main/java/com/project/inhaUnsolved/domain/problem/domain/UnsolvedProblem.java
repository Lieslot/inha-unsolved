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
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
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

    @OneToMany(mappedBy = "problem", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ProblemTag> tags;

    @Builder
    public UnsolvedProblem(int number, String name, Tier tier, List<Tag> tags) {
        this.number = number;
        this.name = name;
        this.tier = tier;
        this.tags = tags.stream()
                .map(tag -> ProblemTag.builder().problem(this).tag(tag).build())
                .collect(Collectors.toSet());

    }

    public void renewTags(List<Tag> tags) {

        List<ProblemTag> newProblemTags = tags.stream()
                                                .map(tag -> ProblemTag.builder().problem(this).tag(tag).build())
                                                .toList();


        this.tags.removeIf(existingTag -> !newProblemTags.contains(existingTag));

        newProblemTags.forEach(newTag -> {
            if (!this.tags.contains(newTag)) {
                this.tags.add(newTag);
            }
        });

    }

    public void renewName(String name) {
        this.name = name;
    }

    public void renewTier(Tier tier) {
        this.tier = tier;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UnsolvedProblem other = (UnsolvedProblem) obj;

        return this.name.equals(other.name) && this.tier == other.tier
                && this.tags.equals(other.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.tier, this.tags);
    }
}
