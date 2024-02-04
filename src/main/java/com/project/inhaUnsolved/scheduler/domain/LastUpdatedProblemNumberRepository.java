package com.project.inhaUnsolved.scheduler.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastUpdatedProblemNumberRepository extends JpaRepository<LastUpdatedProblemNumber, Long> {


    Optional<LastUpdatedProblemNumber> findTopByOrderByNumberDesc();

}
