package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolvedProblemRepository extends JpaRepository<SolvedProblem, Integer> {

    boolean existsByNumber(int number);
}
