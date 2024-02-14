package com.project.inhaUnsolved.transaction;


import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class ProgrammaticTransactionService {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    public boolean transactionExecute() {
        EntityTransaction transaction = entityManagerFactory.createEntityManager()
                                                            .getTransaction();
        transaction.begin();
        boolean isExecuted = TransactionSynchronizationManager.isActualTransactionActive();
        transaction.commit();;

        return isExecuted;
    }

    public Object getCreateEntityManagerTransactionName() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(new SolvedProblem(999999));
        var resource = TransactionSynchronizationManager.getResource(transaction);
        transaction.commit();
        return resource;
    }

    public Object getUtilsTransaction() {
        EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        assert em != null;
        em.persist(new SolvedProblem(1000000));

        return TransactionSynchronizationManager.getCurrentTransactionName();
    }
}
