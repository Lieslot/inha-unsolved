package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProblemRepositoryCustom {


    void deleteAllById(Collection<Integer> ids);

    List<Integer> findAllNumber();

//   List<Integer> findAllId(int batchSize);


   void deleteAllByNumber(Collection<Integer> numbers);

    Set<Integer> findAllNumbersIn(Collection<Integer> numbers);
}
