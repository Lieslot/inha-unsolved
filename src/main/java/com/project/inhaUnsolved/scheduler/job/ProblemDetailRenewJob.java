package com.project.inhaUnsolved.scheduler.job;


import com.project.inhaUnsolved.domain.problem.service.NewProblemAddService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@NoArgsConstructor
public class ProblemDetailRenewJob implements Job {

    @Autowired
    private NewProblemAddService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            service.addNewProblem();
        }
        catch (Exception e) {
            log.error("NewProblemAddJob 예외 발생", e);
        }

    }
}
