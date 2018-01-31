package com.chriniko.springbatchexample.exception;

public class RecoverableException extends RuntimeException {

    private final String message;

    public RecoverableException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
