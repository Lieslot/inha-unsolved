package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemNumberOnly;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepositoryCustom;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {

    private final ProblemRepository unsolvedProblemRepository;
    private final ProblemRepositoryCustom unsolvedProblemRepositoryCustom;
    private final SolvedProblemRepository solvedProblemRepository;

    // 기존 미해결 문제 중 새롭게 해결된 문제가 있으면 해결된 문제 목록에 추가

    public void renewUnsolvedProblem(List<UnsolvedProblem> problems) {

        Set<Integer> numbers = problems.stream()
                                     .map(UnsolvedProblem::getNumber)
                                     .collect(Collectors.toSet());

        List<UnsolvedProblem> newSolvedProblems = unsolvedProblemRepository.findAllByNumberIn(numbers);

        List<Integer> newSolvedProblemNumbers = unsolvedProblemRepository.findAllByNumberIn(numbers)
                .stream()
                .map(UnsolvedProblem::getNumber)
                .toList();

        List<SolvedProblem> newSolvedOne = newSolvedProblemNumbers.stream()
                                                                              .map(SolvedProblem::new)
                                                                                      .toList();

        unsolvedProblemRepositoryCustom.deleteAllById(newSolvedProblems.stream()
                .map(UnsolvedProblem::getId)
                .toList());
        solvedProblemRepository.saveAll(newSolvedOne);


    }

    // 지금까지 없던 문제를 추가
    public void addProblems(List<UnsolvedProblem> newProblems) {

        List<Integer> numbers = newProblems.stream()
                                        .map(UnsolvedProblem::getNumber)
                                        .toList();

        Set<Integer> existingUnsolvedOne = unsolvedProblemRepository.findAllByNumberIn(numbers)
                .stream()
                .map(UnsolvedProblem::getNumber)
                .collect(Collectors.toSet());

        Set<Integer> existingSolvedOne = unsolvedProblemRepository.findAllByNumberIn(numbers)
                                                                  .stream()
                                                                  .map(UnsolvedProblem::getNumber)
                                                                  .collect(Collectors.toSet());;

        for (UnsolvedProblem newProblem : newProblems) {
            int newProblemNumber = newProblem.getNumber();

            if (existingSolvedOne.contains(newProblemNumber) ||
                    existingUnsolvedOne.contains(newProblemNumber)) {
                continue;
            }

            unsolvedProblemRepository.save(newProblem);
        }

    }

    // 문제 정보 갱신
    public void renewProblemDetails(List<UnsolvedProblem> problemDetails) {

        List<Integer> numbers = problemDetails.stream()
                                        .map(UnsolvedProblem::getNumber)
                                        .toList();

        Map<Integer, UnsolvedProblem> existingProblems = unsolvedProblemRepository.findAllByNumberIn(numbers)
                                                                         .stream()
                                                                         .collect(Collectors.toMap(
                                                                                 UnsolvedProblem::getNumber,
                                                                                 Function.identity()));

        for (UnsolvedProblem newProblemDetail : problemDetails) {
            UnsolvedProblem existingProblem = existingProblems.get(newProblemDetail.getNumber());

            if (existingProblem == null) {
                continue;
            }

            existingProblem.renewName(newProblemDetail.getName());
            existingProblem.renewTags(newProblemDetail.getTags()); // 태그 정보가 갱신되지 않은 상태에서 문제 발생
            existingProblem.renewTier(newProblemDetail.getTier());

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
