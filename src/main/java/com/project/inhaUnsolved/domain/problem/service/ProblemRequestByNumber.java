package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetail;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProblemRequestByNumber {


    private static final String API_URL = "https://solved.ac/api/v3/problem/lookup?";

    public ProblemRequestByNumber(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    private final RestTemplate restTemplate;

    private ResponseEntity<ProblemDetails> requestProblem(List<String> problemNumbers) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("problemIds", String.join(",", problemNumbers));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(builder.build()
                                            .encode()
                                            .toUri(), HttpMethod.GET, entity,
                ProblemDetails.class);

    }



    public List<UnsolvedProblem> getProblemBy(List<String> problemNumbers) {

        List<UnsolvedProblem> problems = new ArrayList<>();

        while (true) {

            ResponseEntity<ProblemDetails> response = requestProblem(problemNumbers);
            ProblemDetails body = response.getBody();
            if (body == null) {
                break;
            }

            List<ProblemDetail> problemDetails = body.getProblemDetails();
            if (problemDetails.isEmpty()) {
                break;
            }

            problems.addAll(problemDetails.stream()
                                          .filter(ProblemDetail::isSolvable)
                                          .map(ProblemDetail::toUnsolvedProblem)
                                          .toList());
        }

        return problems;

    }

}