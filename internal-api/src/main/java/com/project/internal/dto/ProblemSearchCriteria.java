package com.project.internal.dto;


import lombok.Getter;
import lombok.Setter;

// TODO: validation
@Getter
@Setter
public class ProblemSearchCriteria {
    private int page = 0;
    private String order = "asc";
    private String searchBy = "all";
    private String kw = "";
    private boolean notSolved = false;
}