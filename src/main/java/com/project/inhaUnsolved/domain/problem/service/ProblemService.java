package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import com.project.inhaUnsolved.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository unsolvedProblemRepository;
    private final SolvedProblemRepository solvedProblemRepository;

    public void renewUnsolvedProblem(List<UnsolvedProblem> problems) {

        for (UnsolvedProblem problem : problems) {


            if (!solvedProblemRepository.existsByNumber(problem.getNumber())) {
                continue;
            }
            int solvedProblemNumber = problem.getNumber();

            if (unsolvedProblemRepository.existsByNumber(solvedProblemNumber)) {
                unsolvedProblemRepository.deleteByNumber(solvedProblemNumber);
            }

            solvedProblemRepository.save(new SolvedProblem(solvedProblemNumber));
        }


    }

    public void addNewProblems(List<UnsolvedProblem> newProblems) {

        for (UnsolvedProblem newProblem : newProblems) {

            if (!solvedProblemRepository.existsByNumber(newProblem.getNumber())) {
                continue;
            }

            int newProblemNumber = newProblem.getNumber();

            if (unsolvedProblemRepository.existsByNumber(newProblemNumber)) {
                continue;
            }

            unsolvedProblemRepository.save(newProblem);
        }



    }

    public void renewProblemDetails(List<UnsolvedProblem> unsolvedProblems) {

    }


}
