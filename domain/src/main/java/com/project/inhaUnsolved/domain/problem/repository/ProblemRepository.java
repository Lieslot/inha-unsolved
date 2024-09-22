package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.Problem;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProblemRepository extends JpaRepository<Problem, Integer>, QuerydslPredicateExecutor<Problem> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Problem> findAllByIdIn(Collection<Integer> ids);

    boolean existsByNumber(int number);

    void deleteByNumber(int number);

    void deleteAllByNumberIn(Collection<Integer> numbers);

    @Query(value = "SELECT * FROM Problem ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Problem> findRandomProblems(int limit);

    Page<Problem> findAll(Pageable pageable);

    Page<Problem> findByNameContaining(String name, Pageable pageable);

    List<Problem> findByNumberIn(Collection<Integer> numbers);

    Integer countUnsolvedProblemsByIsSolved(boolean isSolved);
    List<Problem> findByIsSolvedAndNumberIn(boolean isSolved, Collection<Integer> numbers);
}
