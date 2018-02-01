package com.chriniko.springbatchexample.listener.retry;

import com.chriniko.springbatchexample.exception.NonRecoverableException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class DefaultListenerSupport  implements RetryListener {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        System.out.println("DefaultListenerSupport#open");
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        System.out.println("DefaultListenerSupport#close");
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        System.out.println("DefaultListenerSupport#onError");

        if (throwable instanceof NonRecoverableException) {
            context.setExhaustedOnly();
        }

    }
}
