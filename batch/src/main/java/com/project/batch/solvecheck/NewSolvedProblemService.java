package com.project.batch.solvecheck;


import com.project.api.ProblemRequestSolvedByUser;
import com.project.api.UserDetailRequest;
import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.service.ProblemService;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void commitChunkTransaction(List<User> savedUsers, List<UnsolvedProblem> solvedProblems) {

        List<Integer> numbers = solvedProblems.stream()
                                              .map(UnsolvedProblem::getNumber)
                                              .toList();
        problemService.deleteAllUnsolvedProblemByNumbers(numbers);

        List<SolvedProblem> newSolvedProblems = numbers.stream()
                                                       .map(SolvedProblem::new)
                                                       .collect(Collectors.toList());
        problemService.saveAll(newSolvedProblems);

        userService.saveAll(savedUsers);

    }


}
