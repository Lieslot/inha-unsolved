package com.project.inhaUnsolved.scheduler;


import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@RequiredArgsConstructor
@Configuration
@Slf4j
@Profile("!test")
public class SchedulerConfig {

    private final Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {

            scheduler.clear();

            Map paramsMap = new HashMap<>();

            paramsMap.put("executeCount", 1);
            paramsMap.put("date", LocalDateTime.now()
                                               .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            log.info("스케쥴 설정 시작");
//            buildJob(ProblemRenewJob.class, "ProblemRenewJob", paramsMap, "0 0/5 * * * ?");
//            buildJob(NewProblemAddJob.class, "NewProblemAddJob", paramsMap, "0 0/5 * * * ?");
//            buildJob(TagRenewJob.class, "TagRenewJob", paramsMap, "0 0/5 * * * ?");
//            buildJob(ProblemDetailRenewJob.class, "ProblemDetailRenewJob", paramsMap, "0 0/5 * * * ?");

            log.info("스케쥴 설정 완료");

        } catch (Exception e) {

            log.error("addJob error  : {}", e);

        }
    }

    //Job 추가
    public void buildJob(Class<? extends Job> job, String name, Map paramsMap, String cron)
            throws SchedulerException {

        JobDetail jobDetail = buildJobDetail(job, name, paramsMap);
        Trigger trigger = buildCronTrigger(cron);

        if (scheduler.checkExists(jobDetail.getKey())) {
            scheduler.deleteJob(jobDetail.getKey());
        }
        scheduler.scheduleJob(jobDetail, trigger);
    }

    //JobDetail 생성
    public JobDetail buildJobDetail(Class<? extends Job> job, String name, Map paramsMap) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(paramsMap);
        return JobBuilder
                .newJob(job)
                .withIdentity(name)
                .usingJobData(jobDataMap)
                .build();
    }

    //Trigger 생성
    private Trigger buildCronTrigger(String cronExp) {
        return TriggerBuilder.newTrigger()
                             .withSchedule(CronScheduleBuilder.cronSchedule(cronExp)
                                                              .inTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
                             )
                             .build();
    }
}