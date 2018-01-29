package com.chriniko.springbatchexample.listener;

import com.chriniko.springbatchexample.event.JobFinishedEvent;
import com.chriniko.springbatchexample.verifier.StepVerifier;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO rename class.
public class ExportInsurancesVerificationListener implements JobExecutionListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private List<StepVerifier> stepVerifiers;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Do nothing.
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            final List<Map<Boolean, Optional<String>>> verifiersResults = stepVerifiers
                    .stream()
                    .map(StepVerifier::verify)
                    .collect(Collectors.toList());

            final Boolean allStepsOk = verifiersResults
                    .stream()
                    .flatMap(m -> m.keySet().stream())
                    .reduce(true, (acc, elem) -> acc && elem);

            if (!allStepsOk) {
                System.out.println("\n ---Job with name: " + jobExecution.getJobInstance().getJobName() + " finished unsuccessfully ---");

                System.out.println("--- Error Messages ---");
                verifiersResults
                        .stream()
                        .map(Map::values)
                        .flatMap(Collection::stream)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(System.out::println);

                System.out.println();

            } else {
                System.out.println("\n ---Job with name: " + jobExecution.getJobInstance().getJobName() + " finished successfully ---\n");
            }


            System.out.println(Thread.currentThread().getName() + " --- ExportInsurancesVerificationListener#afterJob --- going to publish event: JobFinishedEvent");
            applicationEventPublisher.publishEvent(new JobFinishedEvent(this, jobExecution.getJobConfigurationName()));
        }
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
