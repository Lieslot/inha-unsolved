package com.project.inhaUnsolved.service;

import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepositoryCustom;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepositoryCustom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {

    private final ProblemRepository unsolvedProblemRepository;
    private final ProblemRepositoryCustom unsolvedProblemRepositoryCustom;
    private final SolvedProblemRepository solvedProblemRepository;
    private final SolvedProblemRepositoryCustom solvedProblemRepositoryCustom;

    public List<UnsolvedProblem> findAllByIdIn(List<Integer> ids) {
        return unsolvedProblemRepository.findAllByIdIn(ids);
    }

    public void save(UnsolvedProblem problem) {
        unsolvedProblemRepository.save(problem);
    }

    public List<UnsolvedProblem> saveAllUnsolvedProblems(Collection<UnsolvedProblem> newUnsolvedProblems) {
        return unsolvedProblemRepository.saveAll(newUnsolvedProblems);
    }

    public void saveAll(Collection<SolvedProblem> newSolvedProblems) {

        Set<Integer> numbers = newSolvedProblems.stream()
                                                .map(SolvedProblem::getNumber)
                                                .collect(Collectors.toSet());
        List<SolvedProblem> existingSolvedProblems = solvedProblemRepository.findAllByNumberIn(numbers);

        newSolvedProblems.removeAll(existingSolvedProblems);

        solvedProblemRepository.saveAll(newSolvedProblems);
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
    public List<Integer> findAllSolvedProblemNumbers(int batchSize) {

        AtomicInteger lastId = new AtomicInteger(-1);

        return IntStream.iterate(0, n -> n + 1)
                        .mapToObj(page -> {
                            List<SolvedProblem> numbers = solvedProblemRepositoryCustom.findSolvedProblems(batchSize, lastId.get());
                            if (numbers.isEmpty()) {
                                return new ArrayList<Integer>();
                            }
                            
                            lastId.set(numbers.get(numbers.size()-1).getId());
                            return numbers.stream()
                                          .map(SolvedProblem::getNumber)
                                          .collect(Collectors.toList());
                        })
                        .takeWhile(batch -> !batch.isEmpty())
                        .flatMap(List::stream)
                        .toList();
    }


    public void deleteAllUnsolvedProblemByNumbers(List<Integer> numbers) {
        unsolvedProblemRepository.deleteAllByNumberIn(numbers);
    }

    public Long getSolvedProblemCount() {
        return solvedProblemRepositoryCustom.getSolvedProblemsCount();
    }

    public List<UnsolvedProblem> findRandomUnsolvedProblems(int limit) {

        return unsolvedProblemRepository.findRandomProblems(limit);
    }

}
