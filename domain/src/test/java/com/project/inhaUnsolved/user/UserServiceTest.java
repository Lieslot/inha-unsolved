package com.project.inhaUnsolved.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import com.project.inhaUnsolved.service.UserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {UserService.class})
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void 새로운_유저정보가_주어졌을_때_바뀐_유저_정보를_리턴하는지_테스트() {

        User test1 = new User("test1", 1000);
        User test2 = new User("test2", 1000);
        User test3 = new User("test3", 1000);

        List<User> existingUsers = List.of(test1, test2);

        when(userRepository.findAll()).thenReturn(existingUsers);

        List<User> newUsers = List.of(new User("test2", 1300),
                new User("test1", 1200), test3);

        List<User> renewedUsers = userService.getRenewedUser(newUsers);

        existingUsers = List.of(new User("test1", 1000),
                new User("test2", 1000));

        existingUsers.forEach(user ->

                assertThat(renewedUsers).anyMatch(
                        renewedUser -> renewedUser.getHandle()
                                                  .equals(user.getHandle())
                                && !renewedUser.hasEqualSolvingCount(user)
                )
        );

        assertThat(renewedUsers).contains(test3);


    }


}
