package com.project.inhaUnsolved.problem;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemsDetailResponse;
import com.project.inhaUnsolved.domain.problem.service.ProblemRequestByNumber;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.util.UriComponentsBuilder;

@RestClientTest(ProblemRequestByNumber.class)
public class ProblemRequestByNumberMockTest {

    private static final String API_URL = "https://solved.ac/api/v3/problem/lookup?";


    @Autowired
    MockRestServiceServer server;

    @Autowired
    ProblemRequestByNumber request;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    void setServer() throws JsonProcessingException {

        int startNumber = 1001;
        List<String> problemNumbers = IntStream.range(startNumber, startNumber+100)
                                  .mapToObj(String::valueOf)
                                  .toList();
        System.out.println(String.join(",", problemNumbers));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("problemIds", String.join(",", problemNumbers));

        ProblemsDetailResponse problemsDetailResponse = new ProblemsDetailResponse(100);
        String expectedResponse = objectMapper.writeValueAsString(problemsDetailResponse);

        server.expect(MockRestRequestMatchers.requestTo(builder.encode()
                                                               .build().toUri()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponse, MediaType.APPLICATION_JSON));


    }


    @Test
    void requestNewProblemTest() {

        List<UnsolvedProblem> newProblems = request.getNewProblems(1000);

        System.out.println(newProblems);

    }


}
