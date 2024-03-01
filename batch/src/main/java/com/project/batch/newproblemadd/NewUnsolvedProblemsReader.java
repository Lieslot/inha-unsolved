package com.project.batch.newproblemadd;

import com.project.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.LastUpdatedProblemNumber;
import com.project.batch.dto.NewUnsolvedProblems;
import com.project.inhaUnsolved.domain.problem.repository.LastUpdatedProblemNumberRepository;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.batch.item.ItemStreamReader;


public class NewUnsolvedProblemsReader implements ItemStreamReader<NewUnsolvedProblems> {

    private static final int DEFAULT_NUMBER = 999;
    private final LastUpdatedProblemNumberRepository repository;
    private final ProblemRequestByNumber request;


    public NewUnsolvedProblemsReader(LastUpdatedProblemNumberRepository repository, ProblemRequestByNumber request) {
        this.repository = repository;
        this.request = request;
    }

    @Override
    public NewUnsolvedProblems read()
            throws Exception {

        int lastUpdatedNumber = getLastUpdatedProblemNumber();

        return getProblemDetails(lastUpdatedNumber);

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