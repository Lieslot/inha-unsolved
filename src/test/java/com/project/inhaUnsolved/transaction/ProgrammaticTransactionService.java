package com.project.inhaUnsolved.transaction;


import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
}
