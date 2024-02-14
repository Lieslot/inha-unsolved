package com.project.inhaUnsolved.domain.problem.repository;

import java.util.List;

public interface SolvedProblemRepositoryCustom {
    List<Integer> findNumbers(int batchSize, int lastId);
}
