package com.project.batch.problemrenew;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.ProblemIdNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.List;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemStreamWriter;


public class ProblemDetailRenewWriter implements ItemStreamWriter<ProblemIdNumber> {

    private final ProblemRequestByNumber request;
    private final ProblemDetailRenewService service;

    public ProblemDetailRenewWriter(ProblemRequestByNumber request,
                                    ProblemDetailRenewService service) {
        this.request = request;
        this.service = service;
    }

    @Override
    public void write(Chunk<? extends ProblemIdNumber> chunk) throws Exception {

        List<? extends ProblemIdNumber> items = chunk.getItems();
        List<UnsolvedProblem> newProblemDetails = getNewProblemDetails(items);
        List<Integer> problemIds = items.stream()
                                        .map(ProblemIdNumber::getId)
                                        .toList();
        service.renewProblemDetails(problemIds, newProblemDetails);

    }

    private List<UnsolvedProblem> getNewProblemDetails(List<? extends ProblemIdNumber> newProblemDetails) {
        List<String> problemNumbers = newProblemDetails.stream()
                                                       .map(ProblemIdNumber::getNumber)
                                                       .map(String::valueOf)
                                                       .toList();

        return request.getProblemBy(problemNumbers);
    }


}
