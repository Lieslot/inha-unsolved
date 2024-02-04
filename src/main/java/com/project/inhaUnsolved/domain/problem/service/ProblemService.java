package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepositoryCustom;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
    private final ProblemRepositoryCustom problemRepositoryCustom;

    // 기존 미해결 문제 중 새롭게 해결된 문제가 있으면 해결된 문제 목록에 추가
    public void renewUnsolvedProblem(List<UnsolvedProblem> problems) {

        List<Integer> numbers = problems.stream()
                                        .map(UnsolvedProblem::getNumber)
                                        .collect(Collectors.toList());

        Set<Integer> existingProblems = unsolvedProblemRepositoryCustom.findSetNumbersIn(numbers);

        List<Integer> newSolvedNumber = numbers.stream()
                                             .filter(existingProblems::contains)
                                             .toList();

        List<SolvedProblem> newSolvedOne = newSolvedNumber.stream()
                                                  .map(SolvedProblem::new)
                                                  .toList();

        unsolvedProblemRepositoryCustom.deleteAllByNumber(newSolvedNumber);

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

        Set<Integer> existingSolvedOne = solvedProblemRepository.findAllByNumberIn(numbers)
                                                                .stream()
                                                                .map(SolvedProblem::getNumber)
                                                                .collect(Collectors.toSet());

        for (UnsolvedProblem newProblem : newProblems) {
            int newProblemNumber = newProblem.getNumber();

            if (existingSolvedOne.contains(newProblemNumber) ||
                    existingUnsolvedOne.contains(newProblemNumber)) {
                continue;
            }

            unsolvedProblemRepository.save(newProblem);
        }

    }

    public List<UnsolvedProblem> findAllByIdIn(List<Integer> ids) {
        return unsolvedProblemRepository.findAllByIdIn(ids);
    }

    public void save(UnsolvedProblem problem) {
        unsolvedProblemRepository.save(problem);
    }

    public List<Integer> findProblemNumbersIn(Collection<Integer> numbers) {
        return unsolvedProblemRepositoryCustom.findAllNumbersIn(numbers);
    }

    public List<Integer> findSolvedProblemNumbersIn(Collection<Integer> numbers) {
        return solvedProblemRepository.findAllByNumberIn(numbers)
                .stream()
                .map(SolvedProblem::getNumber)
                .toList();
    }



    @Transactional(readOnly = true)
    public List<Integer> findAllUnsolvedProblemNumbers() {
        return problemRepositoryCustom.findAllNumber();
    }


}
