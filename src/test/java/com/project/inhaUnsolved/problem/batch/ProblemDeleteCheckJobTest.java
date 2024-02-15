package com.project.inhaUnsolved.problem.batch;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.scheduler.deletecheck.ProblemDeleteCheckJob;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ProblemDeleteCheckJobTest extends BatchTestSupport {

    @Autowired
    private ProblemDeleteCheckJob job;

    @MockBean
    private ProblemRequestSolvedByUser request;


}
