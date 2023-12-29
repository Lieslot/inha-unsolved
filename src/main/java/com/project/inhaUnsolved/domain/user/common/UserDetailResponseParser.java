package com.project.inhaUnsolved.domain.user.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inhaUnsolved.domain.user.dto.UserDetail;
import com.project.inhaUnsolved.domain.user.dto.UserDetailsResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class UserDetailResponseParser {

    public static List<UserDetail> parse(ResponseEntity<String> responseEntity)
            throws JsonProcessingException {

        String body = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        UserDetailsResponse userDetailResponse = objectMapper.readValue(body, UserDetailsResponse.class);

        List<UserDetail> userDetails = userDetailResponse.getItems();

        return userDetails;

    }

}
