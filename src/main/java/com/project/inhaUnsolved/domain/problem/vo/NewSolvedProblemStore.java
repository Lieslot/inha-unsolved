package com.project.inhaUnsolved.domain.problem.vo;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.service.ProblemService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.relational.core.sql.Like;

public class NewSolvedProblemStore {

    private static final int TRANSACTION_UNIT = 100;

    private final Set<Integer> check;
    private final List<UnsolvedProblem> problems;


    public NewSolvedProblemStore() {
        this.check = new HashSet<>();
        this.problems = new ArrayList<>();
    }

    public void addSolvedProblems(List<UnsolvedProblem> solvedProblems) {

        List<UnsolvedProblem> newProblems = solvedProblems.stream()
                                                   .filter(solvedProblem ->
                                                           !checkAlreadyStoredOrAdd(solvedProblem.getNumber()))
                                                   .toList();

        problems.addAll(newProblems);
    }

    public boolean needTransaction() {
        return problems.size() >= TRANSACTION_UNIT;
    }


    public List<UnsolvedProblem> flushProblems() {
        List<UnsolvedProblem> flushed = new ArrayList<>(problems);
        problems.clear();
        return flushed;
    }

    private boolean checkAlreadyStoredOrAdd(Integer problemNumber) {

        if (check.contains(problemNumber)) {
            return true;
        }

        check.add(problemNumber);

        return false;

    }

}
