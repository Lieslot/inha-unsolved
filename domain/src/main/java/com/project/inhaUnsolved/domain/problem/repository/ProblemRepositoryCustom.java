package com.project.inhaUnsolved.domain.problem.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public interface ProblemRepositoryCustom {

    List<Integer> findAllNumber();

    Set<Integer> findSetNumbersIn(Collection<Integer> numbers);

    List<Integer> findAllNumbersIn(Collection<Integer> numbers);


}
