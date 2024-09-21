package com.project.inhaUnsolved.domain.problem.repository;


import com.project.inhaUnsolved.domain.problem.domain.DailyRandomProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRandomProblemRepository extends JpaRepository<DailyRandomProblem, Long> {
}
