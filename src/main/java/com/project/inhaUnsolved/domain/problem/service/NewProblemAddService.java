package com.project.inhaUnsolved.domain.problem.service;


import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewProblemAddService {

    private final ProblemService problemService;
    private final ProblemRequestByNumber request;

    public void addNewProblem() {
        int number = getLastProcessedNumber();

        for (;;number += 100) {
            List<String> problemNumbers = IntStream.range(number, number + 100)
                                                   .mapToObj(String::valueOf)
                                                   .toList();
            List<UnsolvedProblem> newProblems = request.getProblemBy(problemNumbers);

            problemService.addNewProblems(newProblems);
        }


    }

    private Integer getLastProcessedNumber() {

        return Integer.max(problemService.findLastSolvedNumber(), problemService.findLastUnsolvedNumber());

    }
}
