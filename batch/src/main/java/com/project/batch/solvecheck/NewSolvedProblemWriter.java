package com.project.batch.solvecheck;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;


public class NewSolvedProblemWriter implements ItemWriter<User> {

    private final NewSolvedProblemService newSolvedProblemService;

    public NewSolvedProblemWriter(NewSolvedProblemService newSolvedProblemService) {

        this.newSolvedProblemService = newSolvedProblemService;
    }

    @Override
    public void write(Chunk<? extends User> chunk) throws Exception {

        if (chunk == null) {
            return;
        }

        List<User> items = (List<User>) chunk.getItems();
        List<UnsolvedProblem> newSolvedProblems = new ArrayList<>();

        for (User user : items) {
            List<UnsolvedProblem> problemsSolvedByUser = newSolvedProblemService.getProblemsSolvedBy(user);
            newSolvedProblems.addAll(problemsSolvedByUser);
        }

        newSolvedProblemService.commitChunkTransaction(items, newSolvedProblems);

    }


}
