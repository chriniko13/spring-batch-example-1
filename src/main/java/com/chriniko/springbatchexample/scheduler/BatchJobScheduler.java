package com.chriniko.springbatchexample.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchJobScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRegistry jobRegistry;

    // run every 5000 msec (i.e., every 5 secs)
    @Scheduled(fixedRate = 5000)
    public void run() throws Exception {

        Job exportJob = jobRegistry.getJob("exportJob");

        JobExecution jobExecution = jobLauncher.run(exportJob, new JobParametersBuilder().toJobParameters());

        System.out.println("jobExecution = " + jobExecution);
    }


}
