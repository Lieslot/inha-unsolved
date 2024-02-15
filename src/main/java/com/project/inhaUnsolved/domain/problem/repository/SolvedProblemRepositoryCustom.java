package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import java.util.List;

public interface SolvedProblemRepositoryCustom {
    List<Integer> findNumbers(int batchSize, int lastId);

    List<SolvedProblem> filterNumberNotIn(List<Integer> numbers);
}
