package com.chriniko.springbatchexample.verifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class ExportInsurancesStepVerifier implements StepVerifier {

    private static final String SELECT_QUERY = "SELECT count(*) FROM spring_batch_example.insurance_tbl";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExportInsurancesStepVerifier(@Qualifier("hikari") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Map<Boolean, Optional<String>> verify() {

        // Note: should check database records count if are the same with (csv file lines - 1).

        Long totalRecordsOfInsuranceTable = jdbcTemplate.queryForObject(SELECT_QUERY, Long.class);

        return (totalRecordsOfInsuranceTable != countLinesOfInsurancesCsvFile())
                ? Collections.singletonMap(false, Optional.of("##### INSURANCES BATCH EXTRACTION NOT COMPLETED SUCCESSFULLY #####"))
                : Collections.singletonMap(true, Optional.empty());
    }

    private long countLinesOfInsurancesCsvFile() {

        try {

            URI uri = Optional
                    .ofNullable(getClass().getClassLoader().getResource("files/FL_insurance_sample.csv"))
                    .orElseThrow(IllegalStateException::new)
                    .toURI();

            return Files.lines(Paths.get(uri)).count() - 1; // Note: -1 is for headers line.

        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
