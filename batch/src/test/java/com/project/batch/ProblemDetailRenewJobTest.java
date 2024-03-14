package com.project.batch;


import static org.mockito.Mockito.when;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.ProblemIdNumber;
import com.project.batch.problemrenew.ProblemDetailRenewJobConfig;
import com.project.batch.problemrenew.ProblemDetailRenewService;
import com.project.batch.problemrenew.ProblemDetailRenewWriter;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


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
        List<ProblemIdNumber> problemMinDetails = new ArrayList<>();
        for (int i = 1000; i <= 1001; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name(String.format("test %d", i))
                                                  .build();
            problemRepository.save(test);
            problemMinDetails.add(new ProblemIdNumber(
                    test.getId()
                    , test.getNumber()));

        }

        List<UnsolvedProblem> newProblemDetail = new ArrayList<>();
        for (int i = 1000; i <= 1001; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name("test")
                                                  .build();

            newProblemDetail.add(test);
        }

        when(request.getProblemBy(List.of("1000", "1001"))).thenReturn(newProblemDetail);

        renewWriter.write(new Chunk<>(problemMinDetails));

        List<UnsolvedProblem> result = problemRepository.findByNumberIn(List.of(1000, 1001));

        result.forEach(problem -> {
            Assertions.assertThat(problem.getName())
                      .isEqualTo("test");
        });

    }


}



