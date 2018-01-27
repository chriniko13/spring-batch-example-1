package com.chriniko.springbatchexample.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class EventsConfiguration {

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {

        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {

            private final AtomicLong id = new AtomicLong();

            @Override
            public Thread newThread(Runnable r) {

                long id = this.id.incrementAndGet();

                Thread t = new Thread(r);
                t.setName("applicationEventMulticaster-worker-" + id);
                t.setDaemon(true);

                return t;
            }
        });

        eventMulticaster.setTaskExecutor(executorService);

        return eventMulticaster;
    }
}
