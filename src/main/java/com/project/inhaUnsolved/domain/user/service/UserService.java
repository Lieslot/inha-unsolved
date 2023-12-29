package com.project.inhaUnsolved.domain.user.service;

import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.dto.UserDetail;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    public void refreshUserDetail(User user) {

        Optional<User> userSearchResult = userRepository.findByHandle(user.getHandle());

        if (userSearchResult.isEmpty()) {
            userRepository.save(user);
        }
        else {
            User existingUser = userSearchResult.get();
            existingUser.renewSolvedCount(user.getSolvingProblemCount());
            userRepository.save(existingUser);
        }

    }





}
