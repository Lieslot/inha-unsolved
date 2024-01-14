package com.project.inhaUnsolved.domain.problem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProblemController {


    @RequestMapping("/sample")
    public String greeting() {
        return "sample";
    }
}
