package com.chriniko.springbatchexample.verifier;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class ExportStarDatasetsStepVerifier implements StepVerifier {

    @Override
    public Map<Boolean, Optional<String>> verify() {
        //TODO add implementation...
        return Collections.singletonMap(false, Optional.of("fucked up!"));
    }
}
