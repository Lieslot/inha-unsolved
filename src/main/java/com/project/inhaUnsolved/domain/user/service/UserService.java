package com.project.inhaUnsolved.domain.user.service;

import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = true)
    public boolean isNewUserOrUserDetailChanged(User user) {

        Optional<User> userSearchResult = userRepository.findByHandle(user.getHandle());
        if (userSearchResult.isEmpty()) {

            return true;
        }

        User existingUser = userSearchResult.get();

        return !existingUser.hasEqualSolvingCount(user);


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
