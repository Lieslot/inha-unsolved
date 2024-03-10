package com.project.api;


import com.project.inhaUnsolved.domain.problem.common.ProblemDetailResponseParser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetail;
import com.project.inhaUnsolved.domain.problem.dto.ProblemsDetailResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class ProblemRequestSolvedByUser {

    private static final String API_URL = "https://solved.ac/api/v3/search/problem";

    public ProblemRequestSolvedByUser(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private final RestTemplate restTemplate;

    private ResponseEntity<String> requestProblem(String handle, int pageNumber) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("query", "s@" + handle)
                                                           .queryParam("page", String.valueOf(pageNumber));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(builder.build()
                                            .encode()
                                            .toUri(), HttpMethod.GET, entity,
                String.class);

    }


    public List<UnsolvedProblem> getProblems(String handle) {

        List<UnsolvedProblem> problems = new ArrayList<>();

        log.info("{} 유저가 푼 문제 요청", handle);

        for (int pageNumber = 1; ; pageNumber++) {

            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ResponseEntity<String> response = requestProblem(handle, pageNumber);

            ProblemsDetailResponse problemsDetailResponse = ProblemDetailResponseParser.parse(response);

            List<ProblemDetail> problemDetails = problemsDetailResponse.getItems();
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
