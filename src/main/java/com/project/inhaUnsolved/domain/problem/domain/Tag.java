package com.project.inhaUnsolved.domain.problem.domain;

import com.project.inhaUnsolved.domain.bridge.ProblemTag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@NoArgsConstructor
public class Tag {

    @Id
    @Column(nullable = false, unique = true)
    private int number;

    private String name;


    @Builder
    Tag(int number, String name, Set<ProblemTag> problems) {
        this.number = number;
        this.name = name;
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tag other = (Tag) obj;

        return this.number == other.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.number);
    }

    @Override
    public String toString() {
        return "{ number:" + number + ", " + name + "}";
    }
}
