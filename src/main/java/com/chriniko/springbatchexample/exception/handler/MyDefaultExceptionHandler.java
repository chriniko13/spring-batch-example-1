package com.chriniko.springbatchexample.exception.handler;

import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;

public class MyDefaultExceptionHandler implements ExceptionHandler {

    @Override
    public void handleException(RepeatContext context, Throwable throwable) throws Throwable {

        // just for logging purposes...
        System.out.println("MyDefaultExceptionHandler#handleException --- throwable == " + throwable);
    }
}
