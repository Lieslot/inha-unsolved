package com.project.inhaUnsolved.domain.problem.repository;

import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Lock;

public interface ProblemRepositoryCustom {


    void deleteAllById(Collection<Integer> ids);

    List<Integer> findAllNumber();

//    List<Integer> findAllId(int batchSize);


   void deleteAllByNumber(Collection<Integer> numbers);

}
