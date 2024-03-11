package com.project.batch.problemrenew;


import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.service.ProblemService;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemDetailRenewService {

    private final ProblemService problemService;

    @Transactional
    public void renewProblemDetails(List<Integer> problemIds,
                                    List<UnsolvedProblem> newProblemDetails) {

        List<UnsolvedProblem> existingProblems = problemService.findAllByIdIn(problemIds);

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
        }


    }


}
