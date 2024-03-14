package com.project.batch.newproblemadd;

import com.project.batch.dto.NewUnsolvedProblems;
import org.springframework.batch.item.ItemStreamReader;


public class NewUnsolvedProblemsReader implements ItemStreamReader<NewUnsolvedProblems> {


    private final NewUnsolvedProblemAddService newProblemAddService;


    public NewUnsolvedProblemsReader(NewUnsolvedProblemAddService newProblemAddService) {

        this.newProblemAddService = newProblemAddService;
    }

    @Override
    public NewUnsolvedProblems read()
            throws Exception {

        int lastUpdatedNumber = newProblemAddService.getLastUpdatedProblemNumber();

        return newProblemAddService.getProblemDetails(lastUpdatedNumber);

    }


}
