package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetail;
import com.project.inhaUnsolved.domain.problem.dto.ProblemNumberOnly;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<UnsolvedProblem, Integer> {

    boolean existsByNumber(int number);

    void deleteByNumber(int number);

    void deleteAllByNumberIn(Collection<Integer> numbers);

//    List<ProblemNumberOnly> findAllNumbers();

    Optional<UnsolvedProblem> findByNumber(int number);
    List<UnsolvedProblem> findAllByNumberIn(Collection<Integer> number);

    Set<UnsolvedProblem> findSetByNumberIn(Collection<Integer> number);

    Optional<ProblemNumberOnly> findTopByOrderByNumberDesc();

}
