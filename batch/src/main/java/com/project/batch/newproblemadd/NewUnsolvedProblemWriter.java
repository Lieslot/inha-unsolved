package com.project.batch.newproblemadd;

import com.project.batch.dto.NewUnsolvedProblems;
import com.project.inhaUnsolved.domain.problem.domain.LastUpdatedProblemNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.List;
import java.util.Set;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class NewUnsolvedProblemWriter implements ItemWriter<NewUnsolvedProblems> {


    private final NewUnsolvedProblemAddService newProblemAddService;

    public NewUnsolvedProblemWriter(NewUnsolvedProblemAddService newSolvedProblemService) {

        this.newProblemAddService = newSolvedProblemService;
    }

    @Override
    public void write(Chunk<? extends NewUnsolvedProblems> chunk) throws Exception {
        List<? extends NewUnsolvedProblems> items = chunk.getItems();
        addProblems(items.get(0));

    }

    public void addProblems(NewUnsolvedProblems newProblems) {

        List<Integer> numbers = newProblems.getUnsolvedProblems()
                                           .stream()
                                           .map(UnsolvedProblem::getNumber)
                                           .sorted()
                                           .toList();

        Set<Integer> existingSolvedOne = newProblemAddService.findSolvedProblemNumbersIn(numbers);
        
        for (UnsolvedProblem newProblem : newProblems) {
            int newProblemNumber = newProblem.getNumber();

            if (existingSolvedOne.contains(newProblemNumber)) {
                continue;
            }
            newProblemAddService.save(newProblem);

        }

        int lastNumber = numbers.get(numbers.size() - 1);

       newProblemAddService.save(new LastUpdatedProblemNumber(lastNumber));


    }
}
