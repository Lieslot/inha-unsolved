package com.project.inhaUnsolved.service;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepositoryCustom;

import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {

    private final ProblemRepository unsolvedProblemRepository;
    private final ProblemRepositoryCustom unsolvedProblemRepositoryCustom;

    public List<UnsolvedProblem> findAllByIdIn(List<Integer> ids) {
        return unsolvedProblemRepository.findAllByIdIn(ids);
    }

    public void save(UnsolvedProblem problem) {
        unsolvedProblemRepository.save(problem);
    }

    public List<UnsolvedProblem> saveAllUnsolvedProblems(Collection<UnsolvedProblem> newUnsolvedProblems) {
        return unsolvedProblemRepository.saveAll(newUnsolvedProblems);
    }




    public List<Integer> findProblemNumbersIn(Collection<Integer> numbers) {
        return unsolvedProblemRepositoryCustom.findAllNumbersIn(numbers);
    }

    public void deleteAllUnsolvedProblemByNumbers(List<Integer> numbers) {
        unsolvedProblemRepository.deleteAllByNumberIn(numbers);
    }

    public Long getSolvedProblemCount() {
        // TODO UnsolvedProblem을 isSolved를 바꾸자
        return 0L;
    }

    public List<UnsolvedProblem> findRandomUnsolvedProblems(int limit) {

        return unsolvedProblemRepository.findRandomProblems(limit);
    }

    public Page<UnsolvedProblem> getPageOf(int page, int chunk) {
        Pageable pageable = PageRequest.of(page, chunk);
        return unsolvedProblemRepository.findAll(pageable);
    }

    public Page<UnsolvedProblem> getPageOf(int page, int chunk, String title) {
        Pageable pageable = PageRequest.of(page, chunk);
        return unsolvedProblemRepository.findByNameContaining(title, pageable);
    }

}
