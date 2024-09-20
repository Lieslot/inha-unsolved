package com.project.inhaUnsolved.domain.problem.collection;

import com.project.inhaUnsolved.domain.problem.domain.Problem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewSolvedProblemStore {


    private final Set<Integer> check;
    private final List<Problem> problems;


    public NewSolvedProblemStore() {
        this.check = new HashSet<>();
        this.problems = new ArrayList<>();
    }

    public NewSolvedProblemStore(Collection<Integer> solvedProblemNumbers) {
        check = new HashSet<>(solvedProblemNumbers);
        problems = new ArrayList<>();
    }


    public List<Problem> flushProblems() {
        List<Problem> flushed = new ArrayList<>(problems);
        problems.clear();
        return flushed;
    }

    public void storeProblem(Problem problem) {
        if (exists(problem.getNumber())) {
            return;
        }
        problems.add(problem);
    }
    public void storeAllProblems(Collection<Problem> problems) {
        problems.forEach(this::storeProblem);
    }

    private boolean exists(Integer problemNumber) {

        if (check.contains(problemNumber)) {
            return true;
        }

        check.add(problemNumber);
        return false;
    }

}
