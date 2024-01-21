package com.project.inhaUnsolved.scheduler;

import com.project.inhaUnsolved.config.AutoWiringSpringBeanJobFactory;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class QtrzConfig {

    private final DataSource dataSource;
    private final ApplicationContext applicationContext;
    private final PlatformTransactionManager platformTransactionManager;


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

    // 빈 팩토리 auto-wire
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }


    public Properties getQuartzProperties() {
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
}
