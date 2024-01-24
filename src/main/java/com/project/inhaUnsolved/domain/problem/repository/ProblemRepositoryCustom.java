package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.Collection;

public interface ProblemRepositoryCustom {


    void deleteAllById(Collection<Integer> ids);


}
