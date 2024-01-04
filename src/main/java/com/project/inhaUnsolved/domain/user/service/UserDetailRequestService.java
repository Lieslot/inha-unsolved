package com.project.inhaUnsolved.domain.user.service;


import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.dto.UserDetail;
import com.project.inhaUnsolved.domain.user.dto.UserDetailsResponse;
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


// 인하대에 소속되어 있는 유저 정보 api 호출 후 저장
@Service
public class UserDetailRequestService {

    private static final String API_URL = "https://solved.ac/api/v3/ranking/in_organization";

    private final RestTemplate restTemplate;


    public UserDetailRequestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();

    }


    private ResponseEntity<UserDetailsResponse> requestUserDetail(int pageNumber) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("organizationId", "356")
                                                           .queryParam("page", String.valueOf(pageNumber));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                UserDetailsResponse.class);

    }

    public List<User> getUserDetail() {

        List<User> users = new ArrayList<>();

        for (int pageNumber = 1; ; pageNumber++) {
            ResponseEntity<UserDetailsResponse> response = requestUserDetail(pageNumber);

            UserDetailsResponse body = response.getBody();

            if (body == null) {
                break;
            }
            List<UserDetail> userDetails = body.getItems();
            userDetails.forEach(userDetail -> users.add(userDetail.toUser()));

            if (userDetails.isEmpty()) {
                break;
            }

        }
        return users;

    }


}
