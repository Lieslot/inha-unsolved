package com.project.inhaUnsolved.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.inhaUnsolved.domain.user.User;
import java.sql.Date;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.cglib.core.Local;

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
