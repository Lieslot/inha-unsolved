package com.project.api.api;


import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.api.TagRequest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TagDetailRequestTest {

    @Autowired
    TagRequest request;

    @Test
    void tagDetailRequestTest() {

        List<Tag> tagDetails = request.getTagDetails();

        Assertions.assertThat(tagDetails).isNotEmpty();

    }
}
