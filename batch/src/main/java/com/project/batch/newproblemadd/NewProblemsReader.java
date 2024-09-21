package com.project.batch.newproblemadd;

import com.project.batch.dto.NewUnsolvedProblems;
import org.springframework.batch.item.ItemReader;


public class NewProblemsReader implements ItemReader<NewUnsolvedProblems> {


    private final NewProblemsAddService newProblemAddService;


    public NewProblemsReader(NewProblemsAddService newProblemAddService) {

        this.newProblemAddService = newProblemAddService;
    }

    @Override
    public NewUnsolvedProblems read() {

        int lastUpdatedNumber = newProblemAddService.getLastUpdatedProblemNumber();

        return newProblemAddService.getProblemDetails(lastUpdatedNumber);

    }


}
