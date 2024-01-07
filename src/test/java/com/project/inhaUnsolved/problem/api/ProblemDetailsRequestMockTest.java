package com.project.inhaUnsolved.problem.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemsDetailResponse;
import com.project.inhaUnsolved.domain.problem.service.ProblemSolvedByUserRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.util.UriComponentsBuilder;


@RestClientTest(value = {ProblemSolvedByUserRequest.class})
//@SpringBootTest
public class ProblemDetailsRequestMockTest {

    private static final String API_URL = "https://solved.ac/api/v3/search/problem";

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ProblemSolvedByUserRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setRequest() throws Exception{

        String handle = "ditn258gh";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("query", "s@" + handle)
                                                           .queryParam("page", String.valueOf(1));

        ProblemsDetailResponse problemsDetailResponse = new ProblemsDetailResponse(100);
        String expectedResponse = objectMapper.writeValueAsString(problemsDetailResponse);


        server.expect(MockRestRequestMatchers.requestTo(builder.encode().build().toUri()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(
                        expectedResponse, MediaType.APPLICATION_JSON));


    }

    @DisplayName("미해결 문제 갱신 요청 테스트")
    @Test
    void problemSolvedByUserRequestTest() {

        List<UnsolvedProblem> problem = request.getProblems("ditn258gh");
    }




}
