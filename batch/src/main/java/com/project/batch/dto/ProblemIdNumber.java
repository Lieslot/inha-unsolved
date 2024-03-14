package com.project.batch.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProblemIdNumber {

    public ProblemIdNumber(Integer id, Integer number) {
        this.id = id;
        this.number = number;
    }

    private Integer id;
    private Integer number;
}
