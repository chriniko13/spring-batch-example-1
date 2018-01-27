package com.chriniko.springbatchexample.cleaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@Component
public class TaskExecutorCleaner {

    @Autowired
    private CountDownLatch countDownLatchForTaskExecutor;

    @Autowired
    private TaskExecutor taskExecutor;

    @PostConstruct
    void init() {

        Executors.newSingleThreadExecutor().submit(() -> {

            try {

                System.out.println(Thread.currentThread().getName() + " --- TaskExecutorCleaner#init --- will wait until all jobs finished and then will shutdown executor.");
                countDownLatchForTaskExecutor.await();
                System.out.println(Thread.currentThread().getName() + " --- TaskExecutorCleaner#init --- all jobs finished, shutting down executor....");

                ((ThreadPoolTaskExecutor) taskExecutor).shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }

        });


    }
}
