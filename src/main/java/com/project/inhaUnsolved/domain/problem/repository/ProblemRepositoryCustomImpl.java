package com.project.inhaUnsolved.domain.problem.repository;


import static com.project.inhaUnsolved.domain.bridge.QProblemTag.problemTag;
import static com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem.unsolvedProblem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class ProblemRepositoryCustomImpl implements ProblemRepositoryCustom {


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

    @Override
    public List<Integer> findAllNumber() {

        int batchSize = 100;

        return IntStream.iterate(0, n -> n + 1)
                        .mapToObj(page -> jpaQueryFactory
                                .select(unsolvedProblem.number)
                                .from(unsolvedProblem)
                                .offset((long) page * batchSize)
                                .limit(batchSize)
                                .fetch())
                        .takeWhile(batch -> !batch.isEmpty())
                        .flatMap(List::stream)
                        .toList();

    }

    @Override
    public void deleteAllByNumber(Collection<Integer> numbers) {

        jpaQueryFactory.delete(unsolvedProblem)
                       .where(unsolvedProblem.number.in(numbers))
                       .execute();

    }

    @Override
    public Set<Integer> findSetNumbersIn(Collection<Integer> numbers) {

        int batchSize = 50;

        return IntStream.iterate(0, n -> n + 1)
                        .mapToObj(page -> jpaQueryFactory
                                .select(unsolvedProblem.number)
                                .from(unsolvedProblem)
                                .where(unsolvedProblem.number.in(numbers))
                                .offset((long) page * batchSize)
                                .limit(batchSize)
                                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                .fetch())
                        .takeWhile(batch -> !batch.isEmpty())
                        .flatMap(List::stream)
                        .collect(Collectors.toSet());

    }

    @Override
    public List<Integer> findAllNumbersIn(Collection<Integer> numbers) {
        return jpaQueryFactory.select(unsolvedProblem.number)
                              .from(unsolvedProblem)
                              .where(unsolvedProblem.number.in(numbers))
                              .fetch();
    }

//    @Override
//    public List<Integer> findAllId(int batchSize) {
//
//        return IntStream.iterate(0, n -> n + 1)
//                 .mapToObj(page -> jpaQueryFactory
//                         .select(unsolvedProblem.number)
//                         .from(unsolvedProblem)
//                         .offset((long) page * batchSize)
//                         .limit(batchSize)
//                         .fetch())
//                 .takeWhile(batch -> !batch.isEmpty())
//                 .flatMap(List::stream)
//                 .toList();
//
//
//    }


}
