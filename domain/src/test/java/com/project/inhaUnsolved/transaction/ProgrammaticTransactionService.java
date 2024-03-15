package com.project.inhaUnsolved.transaction;


import com.project.inhaUnsolved.domain.problem.domain.SolvedProblem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
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
        transaction.commit();
        ;

        return isExecuted;
    }

    public boolean isTransacted() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(new SolvedProblem(999999));
        boolean isTransacted = TransactionSynchronizationManager.isActualTransactionActive();
        transaction.commit();
        return isTransacted;
    }


}
