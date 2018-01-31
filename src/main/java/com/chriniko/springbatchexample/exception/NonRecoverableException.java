package com.chriniko.springbatchexample.exception;

public class NonRecoverableException extends RuntimeException {

    private final String message;

    public NonRecoverableException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}