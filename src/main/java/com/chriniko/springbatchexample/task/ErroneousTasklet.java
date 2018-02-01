package com.chriniko.springbatchexample.task;

import com.chriniko.springbatchexample.exception.NonRecoverableException;
import com.chriniko.springbatchexample.exception.RecoverableException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ErroneousTasklet implements Tasklet {

    @Value("${erroneous.tasklet.break.it.unrecoverable}")
    private boolean breakItUnrecoverable = true;

    @Qualifier("basic")
    @Autowired
    private RetryTemplate retryTemplate;

    private int badTimes = 0;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        return retryTemplate.execute(new RetryCallback<RepeatStatus, RecoverableException>() {
            @Override
            public RepeatStatus doWithRetry(RetryContext retryContext) {
                return doBusinessLogic();
            }
        });
    }


    private RepeatStatus doBusinessLogic() {

        if (breakItUnrecoverable) {
            System.out.println("ErroneousTasklet#doBusinessLogic --- business logic failed...but it will NOT recover");
            throw new NonRecoverableException("ErroneousTasklet#doBusinessLogic --- business logic failed...but it will NOT recover");
        }

        final Random random = new Random();

        for (int i = 0; i <= 100000; i++) {

            if (badTimes < 4) {
                boolean breakIt = random.nextInt(2) == 1;

                if (breakIt) {
                    badTimes++;
                    System.out.println("ErroneousTasklet#doBusinessLogic --- business logic failed...but it will recover");
                    throw new RecoverableException("ErroneousTasklet#doBusinessLogic --- business logic failed...but it will recover");
                }
            }
        }

        return RepeatStatus.FINISHED;
    }
}
