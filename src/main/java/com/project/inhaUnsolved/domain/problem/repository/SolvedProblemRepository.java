package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemNumberOnly;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolvedProblemRepository extends JpaRepository<SolvedProblem, Integer> {

    boolean existsByNumber(int number);


    Optional<ProblemNumberOnly> findTopByOrderByNumberDesc();
}
