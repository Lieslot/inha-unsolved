package com.project.inhaUnsolved.problem.batch;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.scheduler.newproblemadd.NewProblemAddJobConfig;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("dbtest")
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
