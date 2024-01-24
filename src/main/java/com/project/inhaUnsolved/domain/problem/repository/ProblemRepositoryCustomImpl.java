package com.project.inhaUnsolved.domain.problem.repository;


import static com.project.inhaUnsolved.domain.bridge.QProblemTag.*;
import static com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem.*;

import com.project.inhaUnsolved.domain.bridge.QProblemTag;
import com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class ProblemRepositoryCustomImpl implements ProblemRepositoryCustom{


    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public void deleteAllById(Collection<Integer> ids) {

        jpaQueryFactory.delete(problemTag)
                       .where(problemTag.problem.id.in(ids))
                       .execute();


        jpaQueryFactory.delete(unsolvedProblem)
                .where(unsolvedProblem.id.in(ids))
                .execute();
    }
}
