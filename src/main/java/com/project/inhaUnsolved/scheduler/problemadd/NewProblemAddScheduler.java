package com.project.inhaUnsolved.scheduler.problemadd;


import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import com.project.inhaUnsolved.config.AutoWiringSpringBeanJobFactory;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class NewProblemAddScheduler {



    private final DataSource dataSource;
    private final ApplicationContext applicationContext;
    private final PlatformTransactionManager platformTransactionManager;




    // 빈 팩토리 auto-wire
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }


    @Bean
    public Scheduler scheduler(Trigger trigger, JobDetail jobDetail, SchedulerFactoryBean factoryBean) throws SchedulerException {
        Scheduler scheduler = factoryBean.getScheduler();
        JobKey key = jobDetail.getKey();
        if (scheduler.checkExists(key)) {
            scheduler.deleteJob(key);
        }
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        return scheduler;
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(springBeanJobFactory());
        factory.setDataSource(dataSource);
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);
        factory.setTransactionManager(platformTransactionManager);
        factory.setQuartzProperties(getQuartzProperties());
        return factory;
    }

    public Properties getQuartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        } catch (IOException e) {
            log.error("quartzProperties parse error : {}", e);
        }
        return properties;
    }


    @Bean
    public JobDetail jobDetail() {

        return newJob().ofType(NewProblemAddJob.class)
                       .storeDurably().withIdentity(JobKey.jobKey("NewProblemAdd"))
                .withDescription("주기적으로 새로운 문제를 추가함")
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {

        int intervalSec = 1000;

        return newTrigger().forJob(jobDetail)
                           .withIdentity(TriggerKey.triggerKey("Qrtz_Trigger"))
                           .withDescription("Sample trigger")
                           .withSchedule(simpleSchedule()
                                   .withIntervalInSeconds(intervalSec)
                                   .repeatForever()).build();
    }



}
