package com.project.inhaUnsolved.domain.problem.repository;

import static com.project.inhaUnsolved.domain.problem.domain.QSolvedProblem.solvedProblem;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
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
    public List<SolvedProblem> findSolvedProblems(int batchSize, int lastId) {
        return jpaQueryFactory.selectFrom(solvedProblem)
                                                      .where(ltId(lastId))
                                                      .orderBy(solvedProblem.id.desc())
                                                      .limit(batchSize)
                                                      .fetch();

    }

    private BooleanExpression ltId(int id) {

        if (id == -1) {
            return null;
        }
        return solvedProblem.id.lt(id);
    }

    @Override
    public List<SolvedProblem> filterNumberNotIn(List<Integer> numbers) {

        return jpaQueryFactory.select(solvedProblem)
                              .from(solvedProblem)
                              .where(solvedProblem.number.in(numbers))
                              .fetch();

    }
}
