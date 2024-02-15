package com.project.inhaUnsolved.problem.batch.reader;

import com.project.inhaUnsolved.scheduler.deletecheck.NewSolvedProblemReader;
import com.project.inhaUnsolved.scheduler.deletecheck.NewSolvedProblemService;
import com.project.inhaUnsolved.scheduler.dto.ProblemAndUser;
import org.junit.jupiter.api.Test;


public class NewSolvedProblemReaderTest {


    private NewSolvedProblemReader newSolvedProblemReader;

    @Test
    void 읽기_테스트() throws Exception {
        ProblemAndUser read = newSolvedProblemReader.read();
    }



}
