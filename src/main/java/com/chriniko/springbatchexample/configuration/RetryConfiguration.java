package com.chriniko.springbatchexample.configuration;

import com.chriniko.springbatchexample.exception.NonRecoverableException;
import com.chriniko.springbatchexample.exception.handler.MyDefaultExceptionHandler;
import org.springframework.batch.core.step.item.SimpleRetryExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Collections;

@Configuration
@EnableRetry
public class RetryConfiguration {

    @Qualifier("basic")
    @Bean
    public RetryTemplate retryTemplate() {

        final RetryTemplate retryTemplate = new RetryTemplate();

        final FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(2000L);

        final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5);

        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        retryTemplate.registerListener(
                new SimpleRetryExceptionHandler(
                        retryPolicy,
                        new MyDefaultExceptionHandler(),
                        Collections.singletonList(NonRecoverableException.class)
                )
        );

        return retryTemplate;
    }

}
