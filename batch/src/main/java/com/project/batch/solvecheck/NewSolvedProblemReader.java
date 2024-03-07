package com.project.batch.solvecheck;


import com.project.batch.dto.ProblemAndUser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.vo.NewSolvedProblemStore;
import com.project.inhaUnsolved.domain.user.User;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemReader;

public class NewSolvedProblemReader implements ItemReader<ProblemAndUser> {


    private final NewSolvedProblemService newSolvedProblemService;
    private NewSolvedProblemStore newSolvedProblemStore;
    private List<User> userBuffer;

    public NewSolvedProblemReader(NewSolvedProblemService newSolvedProblemService) {
        this.newSolvedProblemService = newSolvedProblemService;

    }

    @PostConstruct
    public void init() {
        List<User> renewedUsers = newSolvedProblemService.requestAndFilterRenewedUsers();
        userBuffer = new ArrayList<>(renewedUsers);

        List<Integer> solvedProblemNumbers = newSolvedProblemService.findAllSolvedProblemNumber();
        newSolvedProblemStore = new NewSolvedProblemStore(solvedProblemNumbers);

    }

    @Override
    public ProblemAndUser read()
            throws Exception {
        Thread.sleep(3000);
        if (userBuffer.isEmpty()) {
            return null;
        }

        while (!userBuffer.isEmpty()) {

            int endIndex = userBuffer.size() - 1;
            User user = userBuffer.get(endIndex);
            List<UnsolvedProblem> problemSolvedByUser = newSolvedProblemService.getProblemsSolvedBy(user);

            newSolvedProblemStore.storeSolvedProblems(problemSolvedByUser);
            newSolvedProblemStore.storeNextSavedUser(user);
            userBuffer.remove(user);

            if (newSolvedProblemStore.needTransaction()) {
                break;
            }

        }

        List<User> nextSavedUsers = newSolvedProblemStore.flushUsers();
        List<UnsolvedProblem> nextSavedProblems = newSolvedProblemStore.flushProblems();
        // null 처리 따로 해주기
        return new ProblemAndUser(nextSavedUsers, nextSavedProblems);
    }
}
