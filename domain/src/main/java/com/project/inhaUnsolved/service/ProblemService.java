package com.project.inhaUnsolved.service;

import com.project.inhaUnsolved.domain.problem.domain.Problem;
import com.project.inhaUnsolved.domain.problem.repository.DailyRandomProblemRepository;
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
    private final ProblemRepository problemRepository;
    private final DailyRandomProblemRepository dailyRandomProblemRepository;

    public List<Problem> findAllByIdIn(List<Integer> ids) {
        return unsolvedProblemRepository.findAllByIdIn(ids);
    }

    public void save(Problem problem) {
        unsolvedProblemRepository.save(problem);
    }

    public List<Problem> saveAllUnsolvedProblems(Collection<Problem> newUnsolvedProblems) {
        return unsolvedProblemRepository.saveAll(newUnsolvedProblems);
    }

    public List<Problem> findDailyRandomProblems() {
        return dailyRandomProblemRepository.findAll().
                stream()
                .map(randomProblem -> problemRepository.findById(randomProblem.getProblemId()).orElseThrow(IllegalStateException::new))
                .toList();


    }

    public List<Integer> findProblemNumbersIn(Collection<Integer> numbers) {
        return unsolvedProblemRepositoryCustom.findAllNumbersIn(numbers);
    }

    public List<Integer> findSolvedProblemNumbersIn(Collection<Integer> numbers) {
        return unsolvedProblemRepository.findByIsSolvedAndNumberIn(true, numbers)
                .stream()
                .map(Problem::getNumber)
                .toList();
    }

    public void changeToSolved(List<Integer> numbers) {
        List<Problem> problems = unsolvedProblemRepository.findByNumberIn(numbers);
        problems.forEach(Problem::solved);
        System.out.println(problems);
        problemRepository.saveAll(problems);
    }

    public Integer getSolvedProblemCount() {

        return unsolvedProblemRepository.countUnsolvedProblemsByIsSolved(true);
    }

    public Long getProblemCount() {
        return unsolvedProblemRepository.count();
    }

    public List<Problem> findRandomUnsolvedProblems(int limit) {

        return unsolvedProblemRepository.findRandomProblems(limit);
    }

    public Page<Problem> getPageOf(int page, int chunk) {
        Pageable pageable = PageRequest.of(page, chunk);
        return unsolvedProblemRepository.findAll(pageable);
    }

    public Page<Problem> getPageOf(int page, int chunk, String title) {
        Pageable pageable = PageRequest.of(page, chunk);
        return unsolvedProblemRepository.findByNameContaining(title, pageable);
    }

}
