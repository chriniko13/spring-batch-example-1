package com.chriniko.springbatchexample.verifier;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class ErroneousTaskletStepVerifier implements StepVerifier {

    @Override
    public Map<Boolean, Optional<String>> verify() {
        return Collections.singletonMap(true, Optional.empty());
    }
}
