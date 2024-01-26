package com.project.inhaUnsolved.domain.user.service;

import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getRenewedUser(List<User> newUserDetails) {

        Map<String, User> existingUsers = userRepository.findAll()
                                                  .stream()
                                                  .collect(Collectors.toMap(User::getHandle, Function.identity()));

        List<User> renewedUsers = new ArrayList<>();

        for (User newUserDetail : newUserDetails) {
            User existingUser = existingUsers.get(newUserDetail.getHandle());

            if (existingUser == null) {
                renewedUsers.add(newUserDetail);
                continue;
            }

            if (existingUser.hasEqualSolvingCount(newUserDetail)) {
                renewedUsers.add(existingUser);
            }
        }

        return renewedUsers;

    }





    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveAll(List<User> users) {
        log.info("유저 저장 시작");

        for (User user : users) {
            Optional<User> searchResult = userRepository.findByHandle(user.getHandle());

            if (searchResult.isEmpty()) {
                log.info("새로운 유저 저장");
                userRepository.save(user);
                continue;
            }
            log.info("유저 저장");

            User existingUser = searchResult.get();
            existingUser.renewSolvedCount(user.getSolvingProblemCount());
            userRepository.save(existingUser);
        }

    }



}
