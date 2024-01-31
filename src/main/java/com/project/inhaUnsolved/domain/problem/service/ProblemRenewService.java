package com.project.inhaUnsolved.domain.problem.service;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.vo.NewSolvedProblemStore;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.api.UserDetailRequest;
import com.project.inhaUnsolved.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemRenewService {


    private final UserDetailRequest userDetailRequest;
    private final ProblemRequestSolvedByUser problemSolvedByUserRequest;
    private final ProblemService problemService;
    private final UserService userService;

    private final PlatformTransactionManager transactionManager;

    private List<User> requestAndFilterRenewedUsers() {
        List<User> userDetail = userDetailRequest.getUserDetail();

        return userService.getRenewedUser(userDetail);
    }


    public void renewUnsolvedProblem() {

        List<User> renewedUsers = requestAndFilterRenewedUsers();

        NewSolvedProblemStore solvedProblemStore = new NewSolvedProblemStore();

        List<User> userBuffer = new ArrayList<>();

        for (User user : renewedUsers) {
            List<UnsolvedProblem> problems = problemSolvedByUserRequest.getProblems(user.getHandle());
            log.info("유저가 푼 모든 문제 api request");

            userBuffer.add(user);
            solvedProblemStore.addSolvedProblems(problems);
            log.info(String.format("%s 유저가 해결 문제 추가", user.getHandle()));

            if (solvedProblemStore.needTransaction()) {
                log.info("트랜잭션해도 될 정도의 문제가 쌓였음");
                saveTransaction(solvedProblemStore.flushProblems(), userBuffer);
            }

        }

        saveTransaction(solvedProblemStore.flushProblems(), userBuffer);

    }


    public void saveTransaction(List<UnsolvedProblem> problems, List<User> userBuffer) {

        TransactionTemplate template = new TransactionTemplate(transactionManager);

        template.execute(status -> {
            try {
                log.info("문제 갱신 트랜잭션 시작");
                userService.saveAll(userBuffer);
                userBuffer.clear();
                problemService.renewUnsolvedProblem(problems);
                return null;
            }
            catch (Exception e) {
                log.error("문제 갱신 트랜잭션 오류 발생", e);

                status.setRollbackOnly();

                return null;
            }

        });

    }


}
