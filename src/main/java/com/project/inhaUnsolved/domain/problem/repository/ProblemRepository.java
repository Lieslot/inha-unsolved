package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemNumberOnly;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ProblemRepository extends JpaRepository<UnsolvedProblem, Integer> {



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<UnsolvedProblem> findAllByNumberIn(Collection<Integer> number);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<UnsolvedProblem> findAllByIdIn(Collection<Integer> ids);

    Set<UnsolvedProblem> findSetByNumberIn(Collection<Integer> number);

    Optional<ProblemNumberOnly> findTopByOrderByNumberDesc();

    boolean existsByNumber(int number);

    void deleteByNumber(int number);

    void deleteAllByNumberIn(Collection<Integer> numbers);

}
