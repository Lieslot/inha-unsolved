package com.project.inhaUnsolved.problem.batch;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.problem.batch.config.TestBatchConfig;
import com.project.inhaUnsolved.scheduler.problemrenew.ProblemDetailRenewJobConfig;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@SpringBatchTest
public class ProblemDetailRenewJobTest {


    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


//    private ProblemRepository problemRepository;


    @Test
    void 읽기_테스트() throws Exception {

        JobParameters jobParameter = new JobParametersBuilder()
                .addLong("date", new Date().getTime())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("problemDetailRenewStep", jobParameter);


        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);
        System.out.println(stepExecution.getCommitCount());
        System.out.println(stepExecution.getReadCount());
        throw new IllegalArgumentException();

    }
}
