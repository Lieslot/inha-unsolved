package com.project.inhaUnsolved.domain.problem.service;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewProblemAddService {


    @Value("${common.last-number-path}")
    private String LAST_NUMBER_PATH;

    private final ProblemService problemService;
    private final ProblemRequestByNumber request;


    public void addNewProblem() {
        int number = getLastProcessedNumber();


        List<UnsolvedProblem> newProblems = new ArrayList<>();

        int lastProcessedNumber = number;

        for (; ; number += 100) {
            List<String> problemNumbers = IntStream.range(number, number + 100)
                                                   .mapToObj(String::valueOf)
                                                   .toList();

            List<UnsolvedProblem> requestedProblems = request.getProblemBy(problemNumbers);

            if (requestedProblems.isEmpty()) {
                break;
            }

            newProblems.addAll(requestedProblems);

            lastProcessedNumber = requestedProblems.get(requestedProblems.size() - 1)
                                           .getNumber();

        }

        problemService.addProblems(newProblems);

        reWriteLastProcessedNumber(lastProcessedNumber);
    }

    private Integer getLastProcessedNumber() {

        File inputFile = new File(LAST_NUMBER_PATH);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            String lastNumber = reader.readLine();

            return Integer.parseInt(lastNumber);

        } catch (Exception e) {
            log.error("이전에 처리했던 마지막 문제 번호 읽기 오류", e);
            return 1000;
        }

    }

    private void reWriteLastProcessedNumber(int number) {

        File outputFile = new File(LAST_NUMBER_PATH);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            writer.write(String.valueOf(number));
            writer.flush();
        } catch (Exception e) {
            log.error("이전에 처리했던 마지막 문제 번호 읽기 오류", e);
            throw new IllegalArgumentException();
        }
    }
}
