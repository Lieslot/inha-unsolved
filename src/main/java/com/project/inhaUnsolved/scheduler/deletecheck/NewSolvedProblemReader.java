package com.project.inhaUnsolved.scheduler.deletecheck;


import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.vo.NewSolvedProblemStore;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.scheduler.dto.ProblemAndUser;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class NewSolvedProblemReader implements ItemReader<ProblemAndUser> {


    private final NewSolvedProblemService newSolvedProblemService;
    private NewSolvedProblemStore newSolvedProblemStore;
    private List<User> userBuffer;

    public NewSolvedProblemReader(NewSolvedProblemService newSolvedProblemService) {
        this.newSolvedProblemService = newSolvedProblemService;

    }

    @PostConstruct
    void init() {
        List<User> renewedUsers = newSolvedProblemService.requestAndFilterRenewedUsers();
        userBuffer = new ArrayList<>(renewedUsers);

        List<Integer> solvedProblemNumbers = newSolvedProblemService.findAllSolvedProblemNumber();
        newSolvedProblemStore = new NewSolvedProblemStore(solvedProblemNumbers);

    }

    @Override
    public ProblemAndUser read()
            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (userBuffer.isEmpty()) {
            return null;
        }

        while (!userBuffer.isEmpty()) {
            int endIndex = userBuffer.size() - 1;
            User user = userBuffer.get(endIndex);
            List<UnsolvedProblem> problemSolvedByUser = newSolvedProblemService.getProblemsSolvedBy(user);

            newSolvedProblemStore.storeSolvedProblems(problemSolvedByUser);
            newSolvedProblemStore.storeNextSavedUser(user);

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
