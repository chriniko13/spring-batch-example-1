package com.chriniko.springbatchexample.exception;

public class ExitCodeException extends RuntimeException {

    private final String exitCode;

    public ExitCodeException(String exitCode) {
        super(exitCode);
        this.exitCode = exitCode;
    }


    public String getExitCode() {
        return exitCode;
    }
}
