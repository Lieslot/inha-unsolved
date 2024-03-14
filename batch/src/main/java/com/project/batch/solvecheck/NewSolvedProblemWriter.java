package com.project.batch.solvecheck;

import com.project.inhaUnsolved.domain.problem.collection.NewSolvedProblemStore;
import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.user.User;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;


public class NewSolvedProblemWriter implements ItemWriter<User> {

    private final NewSolvedProblemService newSolvedProblemService;
    private final NewSolvedProblemStore newSolvedProblemStore;
    public NewSolvedProblemWriter(NewSolvedProblemService newSolvedProblemService) {
        newSolvedProblemStore = new NewSolvedProblemStore();
        this.newSolvedProblemService = newSolvedProblemService;
    }

    @Override
    public void write(Chunk<? extends User> chunk) throws Exception {

        if (chunk == null) {
            return;
        }

        List<User> items = (List<User>) chunk.getItems();


        for (User user : items) {
            List<UnsolvedProblem> problemsSolvedByUser = newSolvedProblemService.getProblemsSolvedBy(user);
            newSolvedProblemStore.storeAllProblems(problemsSolvedByUser);
        }

        List<UnsolvedProblem> newSolvedProblems = newSolvedProblemStore.flushProblems();

        newSolvedProblemService.commitChunkTransaction(items, newSolvedProblems);

    }


}
