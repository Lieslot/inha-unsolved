package com.project.inhaUnsolved.scheduler.problemrenew;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemMinDetail;
import com.project.inhaUnsolved.domain.problem.service.ProblemService;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemDetailRenewService {

    private final ProblemService problemService;

    @Transactional
    public void renewProblemDetails(List<? extends ProblemMinDetail> problemMinDetails,
                                     List<UnsolvedProblem> newProblemDetails) {

        List<UnsolvedProblem> existingProblems = problemService.findAllByIdIn(problemMinDetails.stream()
                                                                                        .map(ProblemMinDetail::getId)
                                                                                        .toList());

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
            problemService.save(existingProblem);
        }


    }



}
