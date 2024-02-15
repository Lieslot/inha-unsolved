package com.project.inhaUnsolved.scheduler.dto;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.user.User;
import java.util.List;
import lombok.Getter;


@Getter
public class ProblemAndUser {

    private final List<User> users;
    private final List<UnsolvedProblem> unsolvedProblems;


    public ProblemAndUser(List<User> users, List<UnsolvedProblem> unsolvedProblems) {
        this.users = users;
        this.unsolvedProblems = unsolvedProblems;
    }
}
