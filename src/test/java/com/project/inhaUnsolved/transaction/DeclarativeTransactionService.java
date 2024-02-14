package com.project.inhaUnsolved.transaction;


import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class DeclarativeTransactionService {




    @Transactional
    public boolean transactionExecute() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }



}
