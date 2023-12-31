package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<UnsolvedProblem, Integer> {

    boolean existsByNumber(int number);
    void deleteByNumber(int number);
}
