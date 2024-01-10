package com.project.inhaUnsolved.user;


import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import com.project.inhaUnsolved.domain.user.api.UserDetailRequestService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDetailRequestServiceTest {

    @Autowired
    private UserDetailRequestService userDetailRequestService;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 정보 API 테스트")
    @Test
    void userDetailApiTest() {
        List<User> userDetail = userDetailRequestService.getUserDetail();

        List<User> all = userRepository.findAll();
        System.out.println(all);

    }

}
