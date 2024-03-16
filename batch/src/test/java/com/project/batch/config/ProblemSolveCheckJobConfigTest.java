package com.project.batch.config;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootTest
public class ProblemSolveCheckJobConfigTest {



    @Autowired
    private ApplicationContext ac;

    @Test
    void bean_등록테스트() {
        Object problemSolveCheck = ac.getBean("problemSolveCheckJob");

        Assertions.assertThat(problemSolveCheck)
                  .isNotNull();

    }

}
