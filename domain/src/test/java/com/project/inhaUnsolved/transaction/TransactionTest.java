package com.project.inhaUnsolved.transaction;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootTest
@ActiveProfiles("test")
public class TransactionTest {


    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    DeclarativeTransactionService declarative;

    @Autowired
    ProgrammaticTransactionService programmatic;

    @Test
    void transactionNotSupportedTest() {
        TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_NOT_SUPPORTED));

        template.execute(status -> {
            boolean isTransactionExecuted = programmatic.transactionExecute();
            ;
            Assertions.assertThat(isTransactionExecuted)
                      .isFalse();

            isTransactionExecuted = declarative.transactionExecute();
            Assertions.assertThat(isTransactionExecuted)
                      .isTrue();
            return null;
        });


    }

    @Test
    void propagationRequiredTest() {

        TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_REQUIRED));
        template.execute(status -> {

            boolean propagatedTransactionResource = programmatic.isTransacted();
            Assertions.assertThat(propagatedTransactionResource)
                      .isEqualTo(true);
            return null;
        });


    }




}
