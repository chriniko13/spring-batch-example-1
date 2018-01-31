package com.chriniko.springbatchexample.subscriber;

import com.chriniko.springbatchexample.exception.ExitCodeException;
import com.chriniko.springbatchexample.verifier.StepVerifier;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class StepVerifierChecker implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    private List<StepVerifier> stepVerifiers;

    @Autowired
    private JobRegistry jobRegistry;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        doCheck();
    }

    private void doCheck() {

        try {

            final Collection<String> jobNames = jobRegistry.getJobNames();
            int countOfSteps = 0;


            for (String jobName : jobNames) {
                String numberOfSteps = jobName.split("#")[1];
                countOfSteps += Integer.parseInt(numberOfSteps);
            }

            if (stepVerifiers.size() != countOfSteps) {
                // Note: you should have a step verifier for every defined step.
                System.out.println("StepVerifierStartupChecker#init --- exit status code: 1");
                throw new ExitCodeException("1");
            }

        } catch (Exception error) {

            /*
                Note:

                every job name should have the following patter: your_job_name#<number of steps>

                for example: myJob1#3

                This means that job with name myJob1 has 3 defined steps.

             */
            System.out.println("StepVerifierStartupChecker#init --- exit status code: 2");
            throw new ExitCodeException("2");
        }


    }

}
