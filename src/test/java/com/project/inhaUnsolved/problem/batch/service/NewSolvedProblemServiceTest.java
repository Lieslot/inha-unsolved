package com.project.inhaUnsolved.problem.batch.service;

import static org.mockito.Mockito.*;

import com.mysql.cj.protocol.x.XProtocolRowInputStream;
import com.project.inhaUnsolved.domain.problem.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.api.UserDetailRequest;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import com.project.inhaUnsolved.problem.batch.BatchTestSupport;
import com.project.inhaUnsolved.problem.batch.config.TestBatchConfig;
import com.project.inhaUnsolved.scheduler.deletecheck.NewSolvedProblemService;
import java.net.URI;
import java.net.URL;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;



@SpringBootTest
@ActiveProfiles("test")
public class NewSolvedProblemServiceTest {

    @Autowired
    private NewSolvedProblemService newSolvedProblemService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private UserDetailRequest userDetailRequest;


    @Test
    void db에_없던_새로운_유저가_있는지_걸러지는지_테스트() {

        List<User> expectedUsers = List.of(new User("1", 10),
                new User("2", 10),
                new User("3", 10));

        when(userDetailRequest.getUserDetail()).thenReturn(expectedUsers);
        List<User> filteredUsers = newSolvedProblemService.requestAndFilterRenewedUsers();

        Assertions.assertThat(filteredUsers).containsAll(expectedUsers);

    }

    @Test
    void db에_있지만_solvedCount가_변경된_유저가_걸러지는지_테스트() {

        User user1 = new User("1", 10);
        User user2 = new User("2", 10);
        User user3 = new User("3", 10);
        List<User> savedUsers = List.of(user1, user2, user3);

        userRepository.saveAll(savedUsers);

        user1.renewSolvedCount(15);
        user2.renewSolvedCount(16);

        List<User> requestedUserDetail = List.of(user1, user2, user3);
        when(userDetailRequest.getUserDetail()).thenReturn(savedUsers);

        List<User> users = newSolvedProblemService.requestAndFilterRenewedUsers();
        List<User> expectedContainedUsers = List.of(user1, user2);
        User expectedNotContainedUser = user3;

        Assertions.assertThat(users).doesNotContain(expectedNotContainedUser);
        Assertions.assertThat(users).containsAll(expectedContainedUsers);

    }




}
