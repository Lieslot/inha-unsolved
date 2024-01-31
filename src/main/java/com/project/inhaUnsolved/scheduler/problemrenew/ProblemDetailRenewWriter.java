package com.project.inhaUnsolved.scheduler.problemrenew;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;


public class ProblemDetailRenewWriter implements ItemWriter<UnsolvedProblem> {

    private final ProblemRequestByNumber request;

    private final EntityManagerFactory entityManagerFactory;

    ProblemDetailRenewWriter(ProblemRequestByNumber request, EntityManagerFactory emf) {
        this.request = request;

        this.entityManagerFactory = emf;

    }
    @Override
    public void write(Chunk<? extends UnsolvedProblem> chunk) throws Exception {

        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
        }

        List<? extends UnsolvedProblem> items = chunk.getItems();



        List<UnsolvedProblem> newProblemDetails = getNewProblemDetails(items);
        renewProblemDetails(items, newProblemDetails, entityManager);

        entityManager.flush();
    }

    private List<UnsolvedProblem> getNewProblemDetails(List<? extends UnsolvedProblem> newProblemDetails) {
        List<String> problemNumbers = newProblemDetails.stream()
                                                       .map(UnsolvedProblem::getNumber)
                                                       .map(String::valueOf)
                                                       .toList();
        ;
        return request.getProblemBy(problemNumbers);
    }

    private void renewProblemDetails(List<? extends UnsolvedProblem> existingProblems,
                                     List<UnsolvedProblem> newProblemDetails, EntityManager em) {

        Map<Integer, UnsolvedProblem> problemMap = existingProblems
                .stream()
                .collect(Collectors.toMap(
                        UnsolvedProblem::getNumber,
                        Function.identity()));

        for (UnsolvedProblem newProblemDetail : newProblemDetails) {
            UnsolvedProblem existingProblem = problemMap.get(newProblemDetail.getNumber());

            if (existingProblem == null) {
                continue;
            }
            existingProblem.renewName(newProblemDetail.getName());
            existingProblem.renewTags(newProblemDetail.getTags()); // 태그 정보가 갱신되지 않은 상태에서 문제 발생
            existingProblem.renewTier(newProblemDetail.getTier());

            if (!em.contains(existingProblem)) {

                em.merge(existingProblem);

            }
        }


    }


}
