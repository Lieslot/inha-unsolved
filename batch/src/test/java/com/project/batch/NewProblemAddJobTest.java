package com.project.batch;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.newproblemadd.NewUnsolvedProblemAddJobConfig;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;


public class NewProblemAddJobTest extends BatchTestSupport {


    @Autowired
    private NewUnsolvedProblemAddJobConfig newProblemAddJobConfig;

    @Autowired
    private ProblemRequestByNumber request;


    @Test
    void 실행_테스트() throws Exception {

        JobParameters jobParameter = new JobParametersBuilder()
                .addLong("date", new Date().getTime())
                .toJobParameters();

        launchJob(newProblemAddJobConfig.newProblemAddJob(), jobParameter);

        StepExecution stepExecution = ((List<StepExecution>) jobExecution.getStepExecutions()).get(0);

        System.out.println(stepExecution.getReadCount());
        System.out.println(stepExecution.getWriteCount());

    }


    @Test
    void 문제_추가테스트() throws Exception {

        launchJob(newProblemAddJobConfig.newProblemAddJob(), null);

        StepExecution stepExecution = ((List<StepExecution>) jobExecution.getStepExecutions()).get(0);


    }


}
