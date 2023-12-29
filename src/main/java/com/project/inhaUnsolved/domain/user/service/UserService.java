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



    private void refreshUserDetail(UserDetail userDetail) {

        Optional<User> userSearchResult = userRepository.findByHandle(userDetail.getHandle());

        if (userSearchResult.isEmpty()) {
            userRepository.save(userDetail.toUser());
        }
        else {
            User user = userSearchResult.get();

            user.renewSolvedCount(user.getSolvingProblemCount());
        }

    }



}
