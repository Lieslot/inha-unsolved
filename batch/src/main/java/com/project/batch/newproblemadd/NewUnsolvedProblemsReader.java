package com.project.batch.newproblemadd;

import com.project.batch.dto.NewUnsolvedProblems;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.batch.item.ItemStreamReader;


public class NewUnsolvedProblemsReader implements ItemStreamReader<NewUnsolvedProblems> {


    private final NewProblemAddService newProblemAddService;


    public NewUnsolvedProblemsReader(NewProblemAddService newProblemAddService) {

        this.newProblemAddService = newProblemAddService;
    }

    @Override
    public NewUnsolvedProblems read()
            throws Exception {

        int lastUpdatedNumber = newProblemAddService.getLastUpdatedProblemNumber();

        return newProblemAddService.getProblemDetails(lastUpdatedNumber);

    }




}
