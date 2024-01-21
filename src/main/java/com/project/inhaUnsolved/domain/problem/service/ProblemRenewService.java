package com.project.inhaUnsolved.domain.problem.service;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.vo.NewSolvedProblemStore;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.api.UserDetailRequest;
import com.project.inhaUnsolved.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemRenewService {
    

    private final UserDetailRequest userDetailRequest;
    private final ProblemRequestSolvedByUser problemSolvedByUserRequest;
    private final ProblemService problemService;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(getClass());


    public void renewUnsolvedProblem() {
        List<User> userDetail = userDetailRequest.getUserDetail();
        List<User> renewedUserHandle = userDetail.stream()
                .filter(userService::isNewUserOrUserDetailChanged)
                .toList();

        NewSolvedProblemStore solvedProblemStore = new NewSolvedProblemStore();

        List<User> userBuffer = new ArrayList<>();

        for (User user : renewedUserHandle) {
            List<UnsolvedProblem> problems = problemSolvedByUserRequest.getProblems(user.getHandle());
            log.info("유저가 푼 모든 문제 api request");

            userBuffer.add(user);
            solvedProblemStore.addSolvedProblems(problems);
            log.info(String.format("%s 유저가 해결 문제 추가",user.getHandle()));

            if (solvedProblemStore.needTransaction()) {
                log.info("트랜잭션해도 될 정도의 문제가 쌓였음");
                saveTransaction(solvedProblemStore.flushProblems(), userBuffer);
            }

        }

        saveTransaction(solvedProblemStore.flushProblems(), userBuffer);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTransaction(List<UnsolvedProblem> problems, List<User> userBuffer) {

        log.info("문제 갱신 트랜잭션 시작");
        userService.saveAll(userBuffer);
        userBuffer.clear();
        problemService.renewUnsolvedProblem(problems);

    }



}
