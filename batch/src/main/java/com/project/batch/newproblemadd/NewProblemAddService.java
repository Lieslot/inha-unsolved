package com.project.batch.newproblemadd;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.NewUnsolvedProblems;
import com.project.inhaUnsolved.domain.problem.domain.LastUpdatedProblemNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.LastUpdatedProblemNumberRepository;
import com.project.inhaUnsolved.service.ProblemService;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NewProblemAddService {

    private static final int DEFAULT_NUMBER = 999;

    private final ProblemService problemService;
    private final LastUpdatedProblemNumberRepository repository;
    private final ProblemRequestByNumber request;


    public Set<Integer> findProblemNumbersIn(Collection<Integer> numbers) {
        return new HashSet<>(problemService.findProblemNumbersIn(numbers));
    }

    public Set<Integer> findSolvedProblemNumbersIn(Collection<Integer> numbers) {
        return new HashSet<>(problemService.findSolvedProblemNumbersIn(numbers));
    }

    public void save(UnsolvedProblem newProblem) {
        problemService.save(newProblem);
    }
    public void save(LastUpdatedProblemNumber lastUpdatedProblemNumber) {
        repository.save(lastUpdatedProblemNumber);
    }

    public Integer getLastUpdatedProblemNumber() {
        Optional<LastUpdatedProblemNumber> search = repository.findTopByOrderByNumberDesc();

        if (search.isEmpty()) {
            return DEFAULT_NUMBER;
        }

        return search.get()
                     .getNumber();
    }

    public NewUnsolvedProblems getProblemDetails(int number) {

        List<String> requestedNumbers = IntStream.range(number + 1, number + 101)
                                                 .mapToObj(String::valueOf)
                                                 .toList();
        List<UnsolvedProblem> newProblems = request.getProblemBy(requestedNumbers);
        //reader가 null 값을 반환하면 step 종료
        if (newProblems.isEmpty()) {
            return null;
        }

        return new NewUnsolvedProblems(newProblems);
    }



}
