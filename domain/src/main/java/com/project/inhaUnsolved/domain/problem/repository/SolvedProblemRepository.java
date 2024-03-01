package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemNumberOnly;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolvedProblemRepository extends JpaRepository<SolvedProblem, Integer> {

    boolean existsByNumber(int number);


    Optional<ProblemNumberOnly> findTopByOrderByNumberDesc();

    List<SolvedProblem> findAllByNumberIn(Collection<Integer> number);

    List<SolvedProblem> findAllByNumberNotIn(Collection<Integer> number);

    void deleteByNumber(int number);

}
