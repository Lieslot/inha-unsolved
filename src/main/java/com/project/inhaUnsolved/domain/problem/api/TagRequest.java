package com.project.inhaUnsolved.domain.problem.api;


import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.dto.TagDetail;
import com.project.inhaUnsolved.domain.problem.dto.TagDetailResponse;
import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TagRequest {

    private static final String API_URL = "https://solved.ac/api/v3/tag/list";

    private final RestTemplate restTemplate;

    public TagRequest(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    private ResponseEntity<TagDetailResponse> requestAllTagDetails() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL);

        RequestEntity<Void> request = RequestEntity.get(builder.encode()
                                                               .build()
                                                               .toUri())
                                                   .accept(MediaType.APPLICATION_JSON)
                                                   .build();

        return restTemplate.exchange(request, TagDetailResponse.class);

    }


    public List<Tag> getTagDetails() {

        TagDetailResponse body = requestAllTagDetails().getBody();

        if (body == null) {
            throw new IllegalStateException("tag body empty");
        }

        return body.getItems()
                   .stream()
                   .map(TagDetail::toTag)
                   .toList();

    }

}

