package com.project.batch.service;


import com.project.batch.BatchTestSupport;
import com.project.batch.config.TestConfig;
import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;


@Import(TestConfig.class)
public class TestJobTest extends BatchTestSupport {

    @Autowired
    private ApplicationContext ac;
    @Autowired
    private ProblemRepository problemRepository;

    @Test
    void 실행_실험() throws Exception {
        for (int i = 1000; i <= 1199; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name(String.format("test %d", i))
                                                  .build();

            problemRepository.save(test);
        }


        Object testJob = ac.getBean("testJob");
        launchJob((Job) testJob, null);
    }


}
