package com.project.batch;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.newproblemadd.NewProblemAddJobConfig;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;


public class NewProblemAddJobTest extends BatchTestSupport {


    @Autowired
    NewProblemAddJobConfig newProblemAddJobConfig;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    ProblemRequestByNumber request;


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

    // 트랜잭션 롤백 테스트
    // 네트워크 에러 시 대응 테스트
    //
}
