package com.project.inhaUnsolved.transaction;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


@SpringBootTest
public class TransactionTest {


    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    DeclarativeTransactionService declarative;

    @Autowired
    ProgrammaticTransactionService programmatic;

    @Test
    void transactionNotSupportedTest() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_NOT_SUPPORTED));

        boolean isTransactionExecuted = programmatic.transactionExecute();;
        Assertions.assertThat(isTransactionExecuted).isFalse();

        isTransactionExecuted = declarative.transactionExecute();
        Assertions.assertThat(isTransactionExecuted).isTrue();

        status.flush();

    }


}
