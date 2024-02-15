package com.project.inhaUnsolved.scheduler.deletecheck;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.scheduler.dto.ProblemAndUser;
import java.util.List;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;


public class NewSolvedProblemWriter implements ItemWriter<ProblemAndUser> {

    private final NewSolvedProblemService newSolvedProblemService;

    public NewSolvedProblemWriter(NewSolvedProblemService newSolvedProblemService) {

        this.newSolvedProblemService = newSolvedProblemService;
    }

    @Override
    public void write(Chunk<? extends ProblemAndUser> chunk) throws Exception {

        if (chunk == null) {
            return;
        }

        ProblemAndUser problemAndUser = chunk.getItems()
                                             .get(0);
        List<User> nextSavedUsers = problemAndUser.getUsers();
        List<UnsolvedProblem> nextDeletedProblems = problemAndUser.getUnsolvedProblems();

        newSolvedProblemService.commitChunkTransaction(nextSavedUsers, nextDeletedProblems);

    }


}
