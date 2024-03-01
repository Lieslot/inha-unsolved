package com.project.inhaUnsolved.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.inhaUnsolved.domain.user.User;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class UserDetail {

    private String handle;

    private Integer solvedCount;


    public User toUser() {
        return User.builder()
                   .handle(this.handle)
                   .solvingProblemCount(solvedCount)
                   .build();
    }
}
