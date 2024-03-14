package com.project.inhaUnsolved.domain.problem.collection;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.user.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewSolvedProblemStore {


    private final Set<Integer> check;
    private final List<UnsolvedProblem> problems;
    private final List<User> users;


    public NewSolvedProblemStore() {
        this.users = new ArrayList<>();
        this.check = new HashSet<>();
        this.problems = new ArrayList<>();
    }

    public NewSolvedProblemStore(Collection<Integer> solvedProblemNumbers) {
        check = new HashSet<>(solvedProblemNumbers);
        users = new ArrayList<>();
        problems = new ArrayList<>();
    }


    public void storeSolvedProblems(List<UnsolvedProblem> solvedProblems) {

        List<UnsolvedProblem> newProblems = solvedProblems.stream()
                                                          .filter(solvedProblem ->
                                                                  !checkAlreadyStoredOrAdd(solvedProblem.getNumber()))
                                                          .toList();

        problems.addAll(newProblems);
    }

    public Integer getUserNumbers() {
        return users.size();
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

    public void storeNextSavedUser(User user) {
        users.add(user);
    }

    public List<User> flushUsers() {
        List<User> flushed = new ArrayList<>(users);
        users.clear();
        return flushed;
    }

}
