package com.project.inhaUnsolved.domain.problem.repository;

import static com.project.inhaUnsolved.domain.problem.domain.QSolvedProblem.solvedProblem;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SolvedProblemRepositoryCustomImpl implements SolvedProblemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Integer> findNumbers(int batchSize, int lastId) {

        return jpaQueryFactory.select(solvedProblem.number)
                       .from(solvedProblem)
                       .where(ltId(lastId))
                       .orderBy(solvedProblem.id.desc())
                .fetch();
    }

    private BooleanExpression ltId(int id) {

        if (id == -1) {
            return null;
        }
        return solvedProblem.id.lt(id);
    }
}
