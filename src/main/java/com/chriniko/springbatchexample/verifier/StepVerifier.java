package com.chriniko.springbatchexample.verifier;

import java.util.Map;
import java.util.Optional;

public interface StepVerifier {

    Map<Boolean, Optional<String>> verify();
}
