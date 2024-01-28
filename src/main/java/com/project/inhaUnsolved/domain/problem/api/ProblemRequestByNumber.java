package com.project.inhaUnsolved.domain.problem.api;

import com.project.inhaUnsolved.domain.problem.common.ProblemDetailsParser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetail;
import java.util.List;
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


    private static final String API_URL = "https://solved.ac/api/v3/problem/lookup";

    public ProblemRequestByNumber(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    private final RestTemplate restTemplate;

    private ResponseEntity<String> requestProblem(List<String> problemNumbers) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("problemIds", String.join(",", problemNumbers));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(builder.build()
                                            .encode()
                                            .toUri(), HttpMethod.GET, entity,
                String.class);

    }


    public List<UnsolvedProblem> getProblemBy(List<String> problemNumbers) {

        ResponseEntity<String> response = requestProblem(problemNumbers);
        List<ProblemDetail> problemDetails = ProblemDetailsParser.parse(response);

        if (problemDetails == null) {
            throw new IllegalStateException("body empty");
        }

        return problemDetails.stream()
                             .filter(ProblemDetail::isSolvable)
                             .map(ProblemDetail::toUnsolvedProblem)
                             .toList();

    }

}
