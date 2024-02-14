package com.project.inhaUnsolved.transaction;


import jakarta.annotation.security.RunAs;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;


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

    }

    @Test
    void programmaticTransactionPropagationTest() {

        TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_REQUIRED));
        template.execute(status -> {

            Object propagatedTransactionResource = programmatic.getCreateEntityManagerTransactionName();
            Object currentTransactionResource = programmatic.getUtilsTransaction();
            Assertions.assertThat(propagatedTransactionResource).isEqualTo(currentTransactionResource);
            return null;
        });


    }


}
