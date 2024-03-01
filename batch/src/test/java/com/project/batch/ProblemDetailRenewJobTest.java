package com.project.batch;


import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.ProblemMinDetail;
import com.project.batch.problemrenew.ProblemDetailRenewJobConfig;
import com.project.batch.problemrenew.ProblemDetailRenewService;
import com.project.batch.problemrenew.ProblemDetailRenewWriter;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;

import com.project.inhaUnsolved.domain.user.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;



public class ProblemDetailRenewJobTest extends BatchTestSupport {

    @MockBean
    private ProblemRequestByNumber request;
    @Autowired
    private ProblemDetailRenewService service;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemDetailRenewJobConfig jobConfig;


}
