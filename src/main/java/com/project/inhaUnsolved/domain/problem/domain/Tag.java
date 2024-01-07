package com.project.inhaUnsolved.domain.problem.domain;

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
public class Tag {

    @Id
    @Column(nullable = false, unique = true)
    private int number;

    private String name;


    @Builder
    Tag(int number, String name) {
        this.number = number;
        this.name = name;
    }

}
