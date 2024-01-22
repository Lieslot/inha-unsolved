package com.project.inhaUnsolved.scheduler.job;

import com.project.inhaUnsolved.domain.problem.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class TagRenewJob implements Job {


    private TagService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            service.renewTagDetails();
        }
        catch (Exception e) {
            log.error("TagRenewJob 실행 중 오류 발생", e);
        }

    }
}
