package com.project.batch.newproblemadd;

import com.project.batch.dto.NewUnsolvedProblems;
import com.project.inhaUnsolved.domain.problem.domain.LastUpdatedProblemNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.LastUpdatedProblemNumberRepository;
import com.project.inhaUnsolved.service.ProblemService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class NewProblemAddService {

    private final ProblemService problemService;
    private final LastUpdatedProblemNumberRepository repository;

    @Transactional
    public void addProblems(NewUnsolvedProblems newProblems) {

        List<Integer> numbers = newProblems.getUnsolvedProblems()
                                           .stream()
                                           .map(UnsolvedProblem::getNumber)
                                           .sorted()
                                           .toList();

        Set<Integer> existingUnsolvedOne = new HashSet<>(problemService.findProblemNumbersIn(numbers));

        Set<Integer> existingSolvedOne = new HashSet<>(problemService.findSolvedProblemNumbersIn(numbers));
        for (UnsolvedProblem newProblem : newProblems) {
            int newProblemNumber = newProblem.getNumber();

            if (existingSolvedOne.contains(newProblemNumber) ||
                    existingUnsolvedOne.contains(newProblemNumber)) {
                continue;
            }

            problemService.save(newProblem);
        }

        int lastNumber = numbers.get(numbers.size() - 1);

        repository.save(new LastUpdatedProblemNumber(lastNumber));

    }


}
