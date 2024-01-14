package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemNumberOnly;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {

    private final ProblemRepository unsolvedProblemRepository;
    private final SolvedProblemRepository solvedProblemRepository;

    // 기존 미해결 문제 중 새롭게 해결된 문제가 있으면 해결된 문제 목록에 추가
    public void renewUnsolvedProblem(List<UnsolvedProblem> problems) {

        for (UnsolvedProblem problem : problems) {

            int solvedProblemNumber = problem.getNumber();

            if (unsolvedProblemRepository.existsByNumber(solvedProblemNumber)) {
                unsolvedProblemRepository.deleteByNumber(solvedProblemNumber);
            }

            solvedProblemRepository.save(new SolvedProblem(solvedProblemNumber));
        }


    }
    // 지금까지 없던 문제를 추가
    public void addNewProblems(List<UnsolvedProblem> newProblems) {

        for (UnsolvedProblem newProblem : newProblems) {
            int newProblemNumber = newProblem.getNumber();

            if (solvedProblemRepository.existsByNumber(newProblemNumber)) {
                continue;
            }

            if (unsolvedProblemRepository.existsByNumber(newProblemNumber)) {
                continue;
            }

            unsolvedProblemRepository.save(newProblem);
        }

    }

    // 문제 정보 갱신
    public void renewProblemDetails(List<UnsolvedProblem> newUnsolvedProblemDetails) {

        for (UnsolvedProblem problem : newUnsolvedProblemDetails) {

            Optional<UnsolvedProblem> problemSearchResult = unsolvedProblemRepository.findByNumber(problem.getNumber());
            if (problemSearchResult.isEmpty()) {
                continue;
            }

            UnsolvedProblem existingProblem = problemSearchResult.get();

            if (existingProblem.equals(problem)) {
                continue;
            }

            existingProblem.renewName(problem.getName());
            existingProblem.renewTags(problem.getTags()); // 태그 정보가 갱신되지 않은 상태에서 문제 발생
            existingProblem.renewTier(problem.getTier());

            unsolvedProblemRepository.save(existingProblem);
        }

    }

    @Transactional(readOnly = true)
    public Integer findLastSolvedNumber() {

        Optional<ProblemNumberOnly> number = solvedProblemRepository.findTopByOrderByNumberDesc();

        if (number.isEmpty()) {
            return 0;
        }

        return number.get().getNumber();
    }

    @Transactional(readOnly = true)
    public Integer findLastUnsolvedNumber() {
        Optional<ProblemNumberOnly> number = unsolvedProblemRepository.findTopByOrderByNumberDesc();

        if (number.isEmpty()) {
            return 0;
        }

        return number.get().getNumber();
    }



}
