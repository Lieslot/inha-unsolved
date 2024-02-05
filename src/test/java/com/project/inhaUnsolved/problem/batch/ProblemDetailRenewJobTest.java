package com.project.inhaUnsolved.problem.batch;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.scheduler.dto.ProblemMinDetail;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.scheduler.problemrenew.ProblemDetailRenewJobConfig;
import com.project.inhaUnsolved.scheduler.problemrenew.ProblemDetailRenewService;
import com.project.inhaUnsolved.scheduler.problemrenew.ProblemDetailRenewWriter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.Date;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;



@ActiveProfiles("dbtest")
public class ProblemDetailRenewJobTest extends BatchTestSupport {

    @Autowired
    private ProblemRequestByNumber request;
    @Autowired
    private ProblemDetailRenewService service;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemDetailRenewJobConfig jobConfig;

    @Test
    void 실행_테스트() throws Exception {

        JobParameters jobParameter = new JobParametersBuilder()
                .addLong("date", new Date().getTime())
                .toJobParameters();

        launchJob(jobConfig.problemDetailRenewJob(), jobParameter);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);
        System.out.println(stepExecution.getCommitCount());
        System.out.println(stepExecution.getReadCount());

    }

    @Transactional
    @Test
    void 쓰기_테스트() throws Exception {

        ProblemDetailRenewWriter renewWriter = new ProblemDetailRenewWriter(request, service);

        List<UnsolvedProblem> problems = problemRepository.findAllByNumberIn(List.of(1000, 1001));
        problems.forEach(problem -> problem.renewName("test"));
        problemRepository.saveAll(problems);

        List<? extends ProblemMinDetail> problemMinDetails = problems.stream()
                                                                     .map(problem -> new ProblemMinDetail(problem.getId()
                                                                     ,problem.getNumber()))
                                                                             .toList();

        renewWriter.write(new Chunk<>(problemMinDetails));

        problems = problemRepository.findAllByNumberIn(List.of(1000, 1001));

        problems.forEach(problem -> {
            Assertions.assertThat(problem.getName()).isNotEqualTo("test");
        });


    }
}
