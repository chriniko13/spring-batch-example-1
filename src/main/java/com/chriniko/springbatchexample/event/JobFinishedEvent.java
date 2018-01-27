package com.chriniko.springbatchexample.event;

import org.springframework.context.ApplicationEvent;

public class JobFinishedEvent extends ApplicationEvent {

    private final String jobName;

    public JobFinishedEvent(Object source, String jobName) {
        super(source);
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    @Override
    public String toString() {
        return "JobFinishedEvent{" +
                "jobName='" + jobName + '\'' +
                '}';
    }
}
