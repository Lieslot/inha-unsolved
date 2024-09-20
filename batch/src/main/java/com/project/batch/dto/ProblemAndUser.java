package com.project.batch.dto;

import com.project.inhaUnsolved.domain.problem.domain.Problem;
import com.project.inhaUnsolved.domain.user.User;
import java.util.List;
import lombok.Getter;


@Getter
public class ProblemAndUser {

    private final List<User> users;
    private final List<Problem> problems;


    public ProblemAndUser(List<User> users, List<Problem> problems) {
        this.users = users;
        this.problems = problems;
    }
}
