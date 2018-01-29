package com.chriniko.springbatchexample.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.util.concurrent.TimeUnit;

public class PerformanceLoggingStepExecutionListener implements StepExecutionListener {

    private long startTimeInNanos;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        startTimeInNanos = System.nanoTime();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        String stepName = stepExecution.getStepName();
        ExitStatus exitStatus = stepExecution.getExitStatus();

        if (exitStatus.getExitCode().equals("COMPLETED")) {

            long totalTimeInNanos = System.nanoTime() - startTimeInNanos;

            long totalTimeInMs = TimeUnit.MILLISECONDS.convert(totalTimeInNanos, TimeUnit.NANOSECONDS);
            long totalTimeInS = TimeUnit.SECONDS.convert(totalTimeInNanos, TimeUnit.NANOSECONDS);

            System.out.println("\n[stepName = " + stepName + "] --- total time: " + totalTimeInMs + "ms, " + totalTimeInS + "s" + "\n");

        } else {
            System.out.println("\n[stepName = " + stepName + "] --- execution not completed, exit status = " + exitStatus + "\n");
        }


        return null;
    }

}
