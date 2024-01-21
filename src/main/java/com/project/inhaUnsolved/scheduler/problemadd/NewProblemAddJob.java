package com.project.inhaUnsolved.scheduler.problemadd;

import com.project.inhaUnsolved.domain.problem.service.NewProblemAddService;
import lombok.NoArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class NewProblemAddJob implements Job {



    @Autowired
    private NewProblemAddService service;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        service.addNewProblem();
    }
}
