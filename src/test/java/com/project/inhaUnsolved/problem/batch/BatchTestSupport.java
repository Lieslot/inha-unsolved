package com.project.inhaUnsolved.problem.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.BatchStatus;

import org.springframework.batch.core.scope.context.JobContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.batch.test.context.SpringBatchTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import jakarta.persistence.EntityTransaction;

@SpringBootTest
@SpringBatchTest
@TestInstance(Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = AutowireMode.ALL)
public abstract class BatchTestSupport {

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    protected EntityManager entityManager;

    protected JPAQueryFactory queryFactory;

    protected JobExecution jobExecution;

    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }

    protected JPAQueryFactory getQueryFactory() {
        if (queryFactory == null) {
            queryFactory = new JPAQueryFactory(getEntityManager());
        }
        return queryFactory;
    }

    protected void launchJob(Job job, JobParameters jobParameters) throws Exception {
        jobLauncherTestUtils.setJob(job);
        this.jobExecution = jobLauncherTestUtils.launchJob(jobParameters != null ? jobParameters : jobLauncherTestUtils.getUniqueJobParameters());
    }

    protected void launchStep(String stepName, JobParameters jobParameters, ExecutionContext executionContext) throws Exception {
        this.jobExecution = jobLauncherTestUtils.launchStep(stepName, jobParameters != null ? jobParameters : jobLauncherTestUtils.getUniqueJobParameters(), executionContext);
    }

    protected void thenBatchCompleted() {
        then(BatchStatus.COMPLETED).isEqualTo(jobExecution.getStatus());
    }

    protected void thenBatchStatus(BatchStatus batchStatus) {
        then(batchStatus).isEqualTo(jobExecution.getStatus());
    }

    @Transactional
    protected <T> T save(T entity) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.clear();
        return entity;
    }

    @Transactional
    protected <T> List<T> saveAll(List<T> entities) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        for (T entity : entities) {
            em.persist(entity);
        }
        transaction.commit();
        em.clear();
        return entities;
    }

    protected <T> void deleteAll(Class<T> entityClass) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createQuery("DELETE FROM " + entityClass.getSimpleName()).executeUpdate();
        transaction.commit();
    }
}
