package com.project.batch.problemrenew;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.ProblemIdNumber;
import com.project.inhaUnsolved.domain.problem.domain.Problem;
import java.util.List;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;


public class ProblemDetailWriter implements ItemWriter<ProblemIdNumber> {

    private final ProblemRequestByNumber request;
    private final ProblemDetailRenewService service;

    public ProblemDetailWriter(ProblemRequestByNumber request,
                               ProblemDetailRenewService service) {
        this.request = request;
        this.service = service;
    }

    @Override
    public void write(Chunk<? extends ProblemIdNumber> chunk) throws Exception {

        List<? extends ProblemIdNumber> items = chunk.getItems();
        List<Problem> newProblemDetails = getNewProblemDetails(items);
        List<Integer> problemIds = items.stream()
                                        .map(ProblemIdNumber::getId)
                                        .toList();
        service.renewProblemDetails(problemIds, newProblemDetails);

    }

    private List<Problem> getNewProblemDetails(List<? extends ProblemIdNumber> newProblemDetails) {
        List<String> problemNumbers = newProblemDetails.stream()
                                                       .map(ProblemIdNumber::getNumber)
                                                       .map(String::valueOf)
                                                       .toList();

        return request.getProblemBy(problemNumbers);
    }


}
