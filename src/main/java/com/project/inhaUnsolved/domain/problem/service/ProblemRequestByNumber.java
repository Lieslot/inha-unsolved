package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetail;
import com.project.inhaUnsolved.domain.problem.dto.ProblemsDetailResponse;
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


    private static final String API_URL = "https://solved.ac/api/v3/search/problem/lookup";

    public ProblemRequestByNumber(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    private final RestTemplate restTemplate;

    public ResponseEntity<ProblemsDetailResponse> requestProblem(List<String> problemNumbers) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("problemIds", String.join(",", problemNumbers));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(builder.build()
                                            .encode()
                                            .toUri(), HttpMethod.GET, entity,
                ProblemsDetailResponse.class);

    }


    public List<UnsolvedProblem> getNewProblems(int prevStartNumber) {

        List<UnsolvedProblem> problems = new ArrayList<>();

        for (int startNumber = prevStartNumber + 1; ; startNumber += 100) {

            List<String> problemNumbers = IntStream.range(startNumber, startNumber + 100)
                                                   .mapToObj(String::valueOf)
                                                   .toList();

            ResponseEntity<ProblemsDetailResponse> response = requestProblem(problemNumbers);
            ProblemsDetailResponse body = response.getBody();
            if (body == null) {
                break;
            }

            List<ProblemDetail> problemDetails = body.getItems();
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

    public List<UnsolvedProblem> getNewUnsolvedProblemDetails(List<String> problemNumbers) {

        List<UnsolvedProblem> problems = new ArrayList<>();

        while (true) {

            ResponseEntity<ProblemsDetailResponse> response = requestProblem(problemNumbers);
            ProblemsDetailResponse body = response.getBody();
            if (body == null) {
                break;
            }

            List<ProblemDetail> problemDetails = body.getItems();
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
