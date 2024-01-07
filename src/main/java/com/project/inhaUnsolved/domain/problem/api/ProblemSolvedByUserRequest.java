package com.project.inhaUnsolved.domain.problem.api;


import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetail;
import com.project.inhaUnsolved.domain.problem.dto.ProblemsDetailResponse;
import java.util.ArrayList;
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
public class ProblemSolvedByUserRequest {

    private static final String API_URL = "https://solved.ac/api/v3/search/problem";

    public ProblemSolvedByUserRequest(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private final RestTemplate restTemplate;

    private ResponseEntity<ProblemsDetailResponse> requestProblem(String handle, int pageNumber) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("query", "s@" + handle)
                                                           .queryParam("page", String.valueOf(pageNumber));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(builder.build()
                                            .encode()
                                            .toUri(), HttpMethod.GET, entity,
                ProblemsDetailResponse.class);

    }


    public List<UnsolvedProblem> getProblems(String handle) {

        List<UnsolvedProblem> problems = new ArrayList<>();

        for (int pageNumber = 1; ; pageNumber++) {
            ResponseEntity<ProblemsDetailResponse> response = requestProblem(handle, pageNumber);
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
