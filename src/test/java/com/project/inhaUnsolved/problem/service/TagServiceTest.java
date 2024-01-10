package com.project.inhaUnsolved.problem.service;


import com.project.inhaUnsolved.domain.problem.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TagServiceTest {

    @Autowired
    TagService tagService;

    @Test
    void tagServiceTest() {
        tagService.renewTagDetails();
    }

}
