package com.project.batch;


import static org.mockito.Mockito.when;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.ProblemMinDetail;
import com.project.batch.problemrenew.ProblemDetailRenewJobConfig;
import com.project.batch.problemrenew.ProblemDetailRenewService;
import com.project.batch.problemrenew.ProblemDetailRenewWriter;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;


@ActiveProfiles("test")
public class ProblemDetailRenewJobTest extends BatchTestSupport {

    @MockBean
    private ProblemRequestByNumber request;
    @Autowired
    private ProblemDetailRenewService service;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemDetailRenewJobConfig jobConfig;

//    @Test
//    void 실행_테스트() throws Exception {
//
//        JobParameters jobParameter = new JobParametersBuilder()
//                .addLong("date", new Date().getTime())
//                .toJobParameters();
//        StopWatch stopWatch = new StopWatch();
//
//        stopWatch.start();
//        launchJob(jobConfig.problemDetailRenewJob(), null);
//        stopWatch.stop();
//        System.out.println(stopWatch.prettyPrint());
//        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);
//        System.out.println(stepExecution.getCommitCount());
//        System.out.println(stepExecution.getReadCount());
//
//    }

    @Test
    void 쓰기_테스트() throws Exception {

        ProblemDetailRenewWriter renewWriter = new ProblemDetailRenewWriter(request, service);
        UnsolvedProblem test1 = UnsolvedProblem.builder()
                                               .number(1000)
                                               .tags(new HashSet<>())
                                               .tier(Tier.BRONZE_IV)
                                               .name(String.valueOf("test1"))
                                               .build();
        UnsolvedProblem test2 = UnsolvedProblem.builder()
                                               .number(1001)
                                               .tags(new HashSet<>())
                                               .tier(Tier.BRONZE_IV)
                                               .name("test2")
                                               .build();
        List<UnsolvedProblem> problems = List.of(test1, test2);
        problemRepository.saveAll(problems);
        UnsolvedProblem test3 = UnsolvedProblem.builder()
                                               .number(1000)
                                               .tags(new HashSet<>())
                                               .tier(Tier.BRONZE_IV)
                                               .name(String.valueOf("test"))
                                               .build();
        UnsolvedProblem test4 = UnsolvedProblem.builder()
                                               .number(1001)
                                               .tags(new HashSet<>())
                                               .tier(Tier.BRONZE_IV)
                                               .name("test")
                                               .build();

        List<? extends ProblemMinDetail> problemMinDetails = problems.stream()
                                                                     .map(problem -> new ProblemMinDetail(
                                                                             problem.getId()
                                                                             , problem.getNumber()))
                                                                     .toList();

        List<UnsolvedProblem> newProblemDetail = List.of(test3, test4);
        when(request.getProblemBy(List.of("1000", "1001"))).thenReturn(newProblemDetail);

        renewWriter.write(new Chunk<>(problemMinDetails));

        List<UnsolvedProblem> result = problemRepository.findByNumberIn(List.of(1000, 1001));

        result.forEach(problem -> {
            Assertions.assertThat(problem.getName())
                      .isEqualTo("test");
        });

    }


}



