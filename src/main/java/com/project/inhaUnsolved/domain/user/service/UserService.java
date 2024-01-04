package com.project.inhaUnsolved.domain.user.service;

import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    public List<String> getRenewedUserHandle(List<User> users) {

        List<String> handles = new ArrayList<>();

        for (User user : users) {
            Optional<User> userSearchResult = userRepository.findByHandle(user.getHandle());

            if (userSearchResult.isEmpty()) {
                userRepository.save(user);
                continue;
            }

            User existingUser = userSearchResult.get();

            if (existingUser.getSolvingProblemCount() != user.getSolvingProblemCount()) {
                existingUser.renewSolvedCount(user.getSolvingProblemCount());
                userRepository.save(existingUser);

                handles.add(user.getHandle());
            }

        }

        return handles;
    }


}
