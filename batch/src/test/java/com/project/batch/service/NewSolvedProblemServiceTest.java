package com.project.batch.service;

import static org.mockito.Mockito.when;

import com.project.api.UserDetailRequest;
import com.project.batch.solvecheck.NewSolvedProblemService;
import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.bytebuddy.build.Plugin.Factory.UsingReflection.Priority;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ActiveProfiles("test")
public class NewSolvedProblemServiceTest {

    @Autowired
    private NewSolvedProblemService newSolvedProblemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private SolvedProblemRepository solvedProblemRepository;
    @MockBean
    private UserDetailRequest userDetailRequest;



    @Transactional
    @Test
    void db에_없던_새로운_유저가_있는지_걸러지는지_테스트() {

        List<User> expectedUsers = List.of(new User("1", 10),
                new User("2", 10),
                new User("3", 10));

        when(userDetailRequest.getUserDetail()).thenReturn(expectedUsers);
        List<User> filteredUsers = newSolvedProblemService.requestAndFilterRenewedUsers();

        Assertions.assertThat(filteredUsers)
                  .containsAll(expectedUsers);

    }

    @Transactional
    @Test
    void db에_있지만_solvedCount가_변경된_유저가_걸러지는지_테스트() {

        User user1 = new User("1", 10);
        User user2 = new User("2", 10);
        User user3 = new User("3", 10);
        List<User> savedUsers = List.of(user1, user2, user3);

        userRepository.saveAll(savedUsers);

        User user1change = new User("1", 16);
        User user2change = new User("2", 16);

        List<User> requestedUserDetail = List.of(user1change, user2change, user3);
        when(userDetailRequest.getUserDetail()).thenReturn(requestedUserDetail);

        List<User> users = newSolvedProblemService.requestAndFilterRenewedUsers();
        List<User> expectedContainedUsers = List.of(user1, user2);
        User expectedNotContainedUser = user3;

        Assertions.assertThat(users)
                  .doesNotContain(expectedNotContainedUser);
        Assertions.assertThat(users)
                  .containsAll(expectedContainedUsers);

        Assertions.assertThat(userRepository.findAll().size()).isEqualTo(3);
    }
    @Transactional
    @Test
    void commitChunkTransaction_저장테스트() {

        List<UnsolvedProblem> newSolvedProblems = new ArrayList<>();

        for (int i = 1000; i < 1010; i++) {
            UnsolvedProblem problem = UnsolvedProblem.builder()
                                                     .number(i)
                                                     .tags(new HashSet<>())
                                                     .tier(Tier.BRONZE_IV)
                                                     .name(String.valueOf(i))
                                                     .build();
            problemRepository.save(problem);
            if (i < 1007) {
                newSolvedProblems.add(problem);
            }
        }

        List<User> users = new ArrayList<>();

        newSolvedProblemService.commitChunkTransaction(users, newSolvedProblems);

        List<UnsolvedProblem> remainedUnsolvedProblems = problemRepository.findAll();
        List<SolvedProblem> solvedProblems = solvedProblemRepository.findAll();

        remainedUnsolvedProblems.forEach(
                problem -> Assertions.assertThat(newSolvedProblems)
                                     .allMatch(
                                             newSolvedProblem -> !newSolvedProblem.hasEqual(problem.getNumber()))
        );

        solvedProblems.forEach(
                problem -> Assertions.assertThat(newSolvedProblems)
                                     .anyMatch(
                                             newSolvedProblem -> newSolvedProblem.hasEqual(problem.getNumber()))
        );

        Assertions.assertThat(remainedUnsolvedProblems.size())
                  .isEqualTo(3);
        Assertions.assertThat(solvedProblems.size())
                  .isEqualTo(7);

    }

    @Test
    void commitChunkTransaction_트랜잭션_롤백_테스트() {

        List<UnsolvedProblem> newSolvedProblems = new ArrayList<>();

        for (int i = 1000; i < 1010; i++) {
            UnsolvedProblem problem = UnsolvedProblem.builder()
                                                     .number(i)
                                                     .tags(new HashSet<>())
                                                     .tier(Tier.BRONZE_IV)
                                                     .name(String.valueOf(i))
                                                     .build();
            problemRepository.save(problem);
            if (i < 1007) {
                newSolvedProblems.add(problem);
            }
        }

        List<User> nullUserList = null;

        try {
            newSolvedProblemService.commitChunkTransaction(nullUserList, newSolvedProblems);
        } catch (NullPointerException e) {

        }

        List<UnsolvedProblem> remainedUnsolvedProblems = problemRepository.findAll();
        List<SolvedProblem> solvedProblems = solvedProblemRepository.findAll();

        Assertions.assertThat(remainedUnsolvedProblems.size())
                  .isEqualTo(10);
        Assertions.assertThat(solvedProblems.size())
                  .isEqualTo(0);


    }


}
