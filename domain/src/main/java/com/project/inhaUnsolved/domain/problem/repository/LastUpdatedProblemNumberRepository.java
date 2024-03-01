package com.project.inhaUnsolved.domain.problem.repository;

import com.project.inhaUnsolved.domain.problem.domain.LastUpdatedProblemNumber;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastUpdatedProblemNumberRepository extends JpaRepository<LastUpdatedProblemNumber, Long> {


    Optional<LastUpdatedProblemNumber> findTopByOrderByNumberDesc();

}
