package com.project.inhaUnsolved.domain.problem.service;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.api.UserDetailRequest;
import com.project.inhaUnsolved.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RenewUnsolvedProblemService {

    private final UserDetailRequest userDetailRequest;
    private final ProblemRequestSolvedByUser problemSolvedByUserRequest;
    private final ProblemService problemService;
    private final UserService userService;


    public void renewUnsolvedProblem() {
        List<User> userDetail = userDetailRequest.getUserDetail();
        List<String> renewedUserHandle = userService.getRenewedUserHandle(userDetail);

        List<UnsolvedProblem> distinctSolvedProblem = renewedUserHandle.stream()
                                                                       .map(problemSolvedByUserRequest::getProblems)
                                                                       .flatMap(List::stream)
                                                                       .distinct()
                                                                       .toList();
        problemService.renewUnsolvedProblem(distinctSolvedProblem);
    }

    public void renewProblemDetail() {

    }

}
