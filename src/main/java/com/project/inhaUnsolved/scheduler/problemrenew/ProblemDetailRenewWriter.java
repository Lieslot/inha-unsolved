package com.project.inhaUnsolved.scheduler.problemrenew;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemMinDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;


public class ProblemDetailRenewWriter implements ItemWriter<ProblemMinDetail> {

    private final ProblemRequestByNumber request;
    private final ProblemDetailRenewService service;

    public ProblemDetailRenewWriter(ProblemRequestByNumber request, EntityManagerFactory emf,
                             ProblemDetailRenewService service) {
        this.request = request;
        this.service = service;
    }

    @Override
    public void write(Chunk<? extends ProblemMinDetail> chunk) throws Exception {

        List<? extends ProblemMinDetail> items = chunk.getItems();
        List<UnsolvedProblem> newProblemDetails = getNewProblemDetails(items);
        service.renewProblemDetails(items, newProblemDetails);

    }

    private List<UnsolvedProblem> getNewProblemDetails(List<? extends ProblemMinDetail> newProblemDetails) {
        List<String> problemNumbers = newProblemDetails.stream()
                                                       .map(ProblemMinDetail::getNumber)
                                                       .map(String::valueOf)
                                                       .toList();

        return request.getProblemBy(problemNumbers);
    }



}
