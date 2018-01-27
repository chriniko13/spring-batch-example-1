package com.chriniko.springbatchexample.subscriber;

import com.chriniko.springbatchexample.event.JobFinishedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class JobFinishedEventSubscriber implements ApplicationListener<JobFinishedEvent> {

    @Autowired
    private CountDownLatch countDownLatchForTaskExecutor;


    @Override
    public void onApplicationEvent(JobFinishedEvent event) {

        System.out.println(Thread.currentThread().getName() + " --- JobFinishedEventSubscriber#onApplicationEvent --- processing event: " + event);

        countDownLatchForTaskExecutor.countDown();
    }
}
