package com.project.batch;


import static com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.ProblemIdNumber;
import com.project.batch.problemrenew.ProblemDetailRenewJobConfig;
import com.project.batch.problemrenew.ProblemDetailRenewService;
import com.project.batch.problemrenew.ProblemDetailRenewWriter;
import com.project.inhaUnsolved.domain.bridge.ProblemTag;
import com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.TagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("test")
public class ProblemDetailRenewJobTest extends BatchTestSupport {

    @MockBean
    private ProblemRequestByNumber request;
    @Autowired
    private ProblemDetailRenewService service;
    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ProblemDetailRenewJobConfig jobConfig;


    @AfterEach
    void delete() {
        problemRepository.deleteAll();
    }

    @Test
    void 실행_테스트() throws Exception {
        List<String> problemNumbers = new ArrayList<>();
        List<UnsolvedProblem> problems = new ArrayList<>();
        tagRepository.save(Tag.builder().name("tet1").number(1).build());
        tagRepository.save(Tag.builder().name("tet2").number(2).build());

        for (int i = 1000; i <= 1199; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>(List.of(Tag.builder().name("tet1").number(1).build())))
                                                  .tier(Tier.BRONZE_IV)
                                                  .name(String.format("test %d", i))
                                                  .build();
            UnsolvedProblem changed = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>(List.of(
                                                          Tag.builder().name("tet1").number(1).build(),
                                                          Tag.builder().name("tet2").number(2).build())))
                                                  .tier(Tier.BRONZE_IV)
                                                  .name("test")
                                                  .build();
            problemRepository.save(test);

            problems.add(changed);
            problemNumbers.add(String.valueOf(i));
            if (problems.size() == 100) {
                final String number = problemNumbers.get(0);
                when(request.getProblemBy(argThat(list -> list != null && list.contains(number)))).thenReturn(problems);
                problems = new ArrayList<>();
                problemNumbers = new ArrayList<>();
            }

        }


        launchJob(jobConfig.problemDetailRenewJob(), null);


        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);
        EntityManager entityManager1 = getEntityManager();
        EntityTransaction transaction = entityManager1.getTransaction();

        transaction.begin();
        entityManager1.clear();
        List<UnsolvedProblem> changedProblems = getQueryFactory().selectFrom(unsolvedProblem)
                                                                         .fetch();


        changedProblems.forEach(problem -> {
            problem.getTags().forEach( problemTag -> Hibernate.initialize(problem.getTags()));
            int expectedSize = problem.getTags()
                              .size();
            Assertions.assertThat(problem.getName()).isEqualTo("test");
            Assertions.assertThat(expectedSize).isEqualTo(2);

        });

        transaction.commit();

       Assertions.assertThat(stepExecution.getCommitCount());

    }


    @Test
    void 쓰기_테스트() throws Exception {

        ProblemDetailRenewWriter renewWriter = new ProblemDetailRenewWriter(request, service);
        List<ProblemIdNumber> problemMinDetails = new ArrayList<>();
        for (int i = 1000; i <= 1011; i++) {
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
        List<String> problemNumbers = new ArrayList<>();
        for (int i = 1000; i <= 1011; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name("test")
                                                  .build();

            newProblemDetail.add(test);
            problemNumbers.add(String.valueOf(i));
        }

        when(request.getProblemBy(problemNumbers)).thenReturn(newProblemDetail);

        renewWriter.write(new Chunk<>(problemMinDetails));

        List<UnsolvedProblem> result = problemRepository.findByNumberIn(problemNumbers.stream().
                                                                                      map(Integer::parseInt)
                .collect(Collectors.toList()));

        result.forEach(problem -> {
            Assertions.assertThat(problem.getName())
                      .isEqualTo("test");
        });

    }




}



