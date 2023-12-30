package com.project.inhaUnsolved.domain.problem.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetail;
import com.project.inhaUnsolved.domain.problem.dto.ProblemsDetailResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class ProblemDetailsParser {

    public static List<ProblemDetail> parse(ResponseEntity<String> response)
            throws JsonProcessingException {

        String body = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        ProblemsDetailResponse problemDetailsResponse = objectMapper.readValue(body, ProblemsDetailResponse.class);
        List<ProblemDetail> problemDetails = problemDetailsResponse.getItems();
        System.out.println("problemDetails = " + problemDetails);
        return problemDetails;
    }
}
