package com.project.inhaUnsolved.scheduler.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProblemMinDetail {

    public ProblemMinDetail(Integer id, Integer number) {
        this.id = id;
        this.number = number;
    }

    private Integer id;
    private Integer number;
}
