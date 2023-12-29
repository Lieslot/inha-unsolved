package com.project.inhaUnsolved.user;


import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.dto.UserDetail;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import com.project.inhaUnsolved.domain.user.service.UserDetailRequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest
public class UserDetailRequestServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailRequestService userDetailRequestService;



    @DisplayName("유저 정보 API 테스트")
    @Test
    void userDetailApiTest() {
        List<UserDetail> userDetail = userDetailRequestService.getUserDetail();

        List<User> all = userRepository.findAll();
        System.out.println(all);

    }

}
