package com.project.inhaUnsolved.domain.problem.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetail;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class ProblemDetailsParser {


    public static List<ProblemDetail> parse(ResponseEntity<String> response) {

        String body = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(body, new TypeReference<List<ProblemDetail>>() {
            });

        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return new ArrayList<ProblemDetail>();
        }

    }


}
