package com.chriniko.springbatchexample.configuration;

import com.chriniko.springbatchexample.exception.ExitCodeException;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExitCodeConfiguration {

    @Bean
    ExitCodeExceptionMapper exitCodeExceptionMapper() {
        return exception -> {

            if (exception.getCause() instanceof ExitCodeException) {

                ExitCodeException exitCodeException = (ExitCodeException) exception.getCause();
                return Integer.parseInt(exitCodeException.getExitCode());

            }

            return 17; //Note: Default exit code is 17.
        };
    }
}
