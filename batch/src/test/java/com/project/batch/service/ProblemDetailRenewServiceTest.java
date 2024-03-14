package com.project.batch.service;


import com.project.batch.problemrenew.ProblemDetailRenewService;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class ProblemDetailRenewServiceTest {


    @Autowired
    private ProblemDetailRenewService service;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private EntityManagerFactory emf;


    @Test
    void 더티_체킹_테스트() {

        EntityManager entityManager = emf.createEntityManager();

        List<Integer> problemIds = new ArrayList<>();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        for (int i = 1000; i <= 1003; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name(String.format("test %d", i))
                                                  .build();
            UnsolvedProblem savedOne = entityManager.merge(test);
            if (i == 1003) {
                break;
            }
            problemIds.add(savedOne.getId());

        }
        transaction.commit();

        entityManager.clear(); // 영속성 컨텍스트 비우기

        List<UnsolvedProblem> newProblemDetails = new ArrayList<>();
        for (int i = 1000; i <= 1002; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name("test")
                                                  .build();
            newProblemDetails.add(test);
        }
        service.renewProblemDetails(problemIds, newProblemDetails); // 더티 체킹

        EntityTransaction transaction2 = entityManager.getTransaction();
        transaction2.begin();
        for (int i = 1; i <= 3; i++) {
            UnsolvedProblem problem = entityManager.find(UnsolvedProblem.class, i);
            Assertions.assertThat(problem.getName())
                      .isEqualTo("test");
        }
        Assertions.assertThat(entityManager.find(UnsolvedProblem.class, 4)
                                           .getName())
                  .isNotEqualTo("test");
        transaction2.commit();

    }
}
