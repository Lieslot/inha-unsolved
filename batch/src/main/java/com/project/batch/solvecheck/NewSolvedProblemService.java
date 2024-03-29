package com.project.batch.solvecheck;


import com.project.api.ProblemRequestSolvedByUser;
import com.project.api.UserDetailRequest;
import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.service.ProblemService;
import com.project.inhaUnsolved.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    private final ProblemRequestSolvedByUser problemSolvedByUserRequest;


    public List<User> requestAndFilterRenewedUsers() {
        List<User> newUserDetails = userDetailRequest.getUserDetail();

        Map<String, User> existingUsers = userService.findAll()
                                                     .stream()
                                                     .collect(
                                                             Collectors.toMap(User::getHandle, Function.identity()));

        List<User> renewedUsers = new ArrayList<>();

        for (User newUserDetail : newUserDetails) {
            User existingUser = existingUsers.get(newUserDetail.getHandle());

            if (existingUser == null) {
                renewedUsers.add(newUserDetail);
                continue;
            }

            if (!existingUser.hasEqualSolvingCount(newUserDetail)) {
                renewedUsers.add(newUserDetail);
            }
        }

        return renewedUsers;

    }


    public List<Integer> findAllSolvedProblemNumber() {
        return problemService.findAllSolvedProblemNumbers(batchSize);
    }

    public List<UnsolvedProblem> getProblemsSolvedBy(User user) {
        return problemSolvedByUserRequest.getProblems(user.getHandle());
    }


    @Transactional
    public void commitChunkTransaction(List<User> savedUsers, List<UnsolvedProblem> solvedProblems) {

        List<Integer> numbers = solvedProblems.stream()
                                              .map(UnsolvedProblem::getNumber)
                                              .toList();
        problemService.deleteAllUnsolvedProblemByNumbers(numbers);

        userService.saveAll(savedUsers);


    }


}
