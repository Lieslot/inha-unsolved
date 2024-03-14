package com.project.inhaUnsolved.service;

import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveAll(List<User> users) {

        for (User user : users) {
            Optional<User> searchResult = userRepository.findByHandle(user.getHandle());

            if (searchResult.isEmpty()) {
                userRepository.save(user);
                continue;
            }

            User existingUser = searchResult.get();
            existingUser.renewSolvedCount(user.getSolvingProblemCount());
            userRepository.save(existingUser);
        }

    }

    public Long getUserCount() {
        return userRepository.getUserCount();
    }

}
