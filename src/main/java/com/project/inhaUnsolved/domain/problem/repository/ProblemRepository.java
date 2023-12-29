package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
}
