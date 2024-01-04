package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemNumberOnly;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<UnsolvedProblem, Integer> {

    boolean existsByNumber(int number);

    void deleteByNumber(int number);

    List<ProblemNumberOnly> findAllNumbers();

    Optional<UnsolvedProblem> findByNumber(int number);
}
