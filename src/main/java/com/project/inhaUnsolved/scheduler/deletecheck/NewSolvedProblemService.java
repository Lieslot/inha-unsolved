package com.project.inhaUnsolved.scheduler.deletecheck;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.service.ProblemService;
import com.project.inhaUnsolved.domain.problem.vo.NewSolvedProblemStore;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.api.UserDetailRequest;
import com.project.inhaUnsolved.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewSolvedProblemService {

    private static final int batchSize = 1000;

    private final ProblemService problemService;
    private final UserService userService;
    private final UserDetailRequest userDetailRequest;
    private final ProblemRequestSolvedByUser problemRequestSolvedByUserRequest;

    public List<User> requestAndFilterRenewedUsers() {
        List<User> userDetail = userDetailRequest.getUserDetail();
        return userService.getRenewedUser(userDetail);
    }

    public List<Integer> findAllSolvedProblemNumber() {
        return problemService.findAllSolvedProblemNumbers(batchSize);
    }

    public List<UnsolvedProblem> getProblemsSolvedBy(User user) {
        return problemRequestSolvedByUserRequest.getProblems(user.getHandle());
    }


}
