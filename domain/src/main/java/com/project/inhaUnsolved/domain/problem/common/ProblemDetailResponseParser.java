package com.project.inhaUnsolved.domain.problem.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inhaUnsolved.domain.problem.dto.ProblemsDetailResponse;
import org.springframework.http.ResponseEntity;

public class ProblemDetailResponseParser {

    public static ProblemsDetailResponse parse(ResponseEntity<String> response) {

        String body = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(body, ProblemsDetailResponse.class);

        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException();
        }

    }


}
