package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProblemRepository extends JpaRepository<UnsolvedProblem, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<UnsolvedProblem> findAllByIdIn(Collection<Integer> ids);

    boolean existsByNumber(int number);

    void deleteByNumber(int number);

    void deleteAllByNumberIn(Collection<Integer> numbers);

    @Query(value = "SELECT * FROM Unsolved_Problem ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<UnsolvedProblem> findRandomProblems(int limit);

    Page<UnsolvedProblem> findAll(Pageable pageable);

    Page<UnsolvedProblem> findByNameContaining(String name, Pageable pageable);

    List<UnsolvedProblem> findByNumberIn(Collection<Integer> numbers);
}
