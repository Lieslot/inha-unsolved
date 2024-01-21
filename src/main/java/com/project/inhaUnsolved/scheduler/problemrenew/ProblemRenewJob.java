package com.project.inhaUnsolved.scheduler.problemrenew;

import com.project.inhaUnsolved.domain.problem.service.NewProblemAddService;
import com.project.inhaUnsolved.domain.problem.service.ProblemRenewService;
import com.sun.jdi.event.ExceptionEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
public class ProblemRenewJob implements Job {

    @Autowired
    private ProblemRenewService service;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            service.renewUnsolvedProblem();
        } catch (Exception e) {
            log.error("ProblemRenewJob 예외 발생", e);
        }

    }
}
