package com.project.inhaUnsolved.domain.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.common.UserDetailResponseParser;
import com.project.inhaUnsolved.domain.user.dto.UserDetail;
import com.project.inhaUnsolved.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


// 인하대에 소속되어 있는 유저 정보 api 호출 후 저장
@Service
public class UserDetailRequestService {

    private static final String API_URL = "https://solved.ac/api/v3/ranking/in_organization";

    private final RestTemplate restTemplate;


    public UserDetailRequestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();

    }


    private ResponseEntity<String>  requestUserDetail(int pageNumber) {


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("organizationId", "356")
                .queryParam("page", String.valueOf(pageNumber));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                String.class);

    }

    public List<UserDetail> getUserDetail()  {

        List<UserDetail> userDetails = new ArrayList<>();

        for (int pageNumber = 1;; pageNumber++) {
            ResponseEntity<String> response = requestUserDetail(pageNumber);
            // ExceptionHandler에 대해 공부해보고 개선해보기
            try {
                userDetails.addAll(UserDetailResponseParser.parse(response));
                if (userDetails.isEmpty()) {
                    break;
                }

            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
                throw new IllegalStateException();
            }
        }
        return userDetails;

    }





}
