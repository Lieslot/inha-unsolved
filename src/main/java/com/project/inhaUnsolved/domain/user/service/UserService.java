package com.project.inhaUnsolved.domain.user.service;

import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.dto.UserDetail;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;



    public void refreshUserDetail(User renwedUser) {

        Optional<User> userSearchResult = userRepository.findByHandle(renwedUser.getHandle());

        if (userSearchResult.isEmpty()) {
            userRepository.save(renwedUser);
        }
        else {
            User existingUser = userSearchResult.get();

            if (existingUser.getSolvingProblemCount() != renwedUser.getSolvingProblemCount()) {

            }

            existingUser.renewSolvedCount(renwedUser.getSolvingProblemCount());
            userRepository.save(existingUser);
        }

    }





}
