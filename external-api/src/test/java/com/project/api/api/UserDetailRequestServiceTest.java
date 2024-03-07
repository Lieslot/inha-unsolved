//package com.project.api.api;
//
//
//import com.project.api.UserDetailRequest;
//import com.project.inhaUnsolved.domain.user.User;
//import com.project.inhaUnsolved.service.UserService;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class UserDetailRequestServiceTest {
//
//    @Autowired
//    private UserDetailRequest userDetailRequestService;
//
//    @Autowired
//    private UserService userService;
//
//
//
//    @DisplayName("유저 정보 API 테스트")
//    @Test
//    void userDetailApiTest() {
//        List<User> userDetail = userDetailRequestService.getUserDetail();
//
//    }
//
//    @DisplayName("유저 정보 API 호출 후 저장 테스트")
//    @Test
//    void userDetailSaveAfterRequest() {
//        List<User> userDetail = userDetailRequestService.getUserDetail();
//        userService.saveAll(userDetail);
//
//        List<User> all = userService.findAll();
//
//
//
//    }
//
//}
