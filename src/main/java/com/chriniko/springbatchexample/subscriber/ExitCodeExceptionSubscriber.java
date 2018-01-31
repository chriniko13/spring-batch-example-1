package com.chriniko.springbatchexample.subscriber;

import org.springframework.boot.ExitCodeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ExitCodeExceptionSubscriber {

    @EventListener
    public void exitEvent(ExitCodeEvent event) {
        System.out.println("-- ExitCodeEvent --");
        System.out.println("exit code: " + event.getExitCode());
    }

}
