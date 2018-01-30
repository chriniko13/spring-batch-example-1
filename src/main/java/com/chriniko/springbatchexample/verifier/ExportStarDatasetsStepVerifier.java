package com.chriniko.springbatchexample.verifier;

import com.chriniko.springbatchexample.util.GZipFile;
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
public class ExportStarDatasetsStepVerifier implements StepVerifier {

    private static final String SELECT_QUERY = "SELECT count(*) FROM spring_batch_example.star_dataset_tbl";

    private final JdbcTemplate jdbcTemplate;
    private final GZipFile gZipFile;

    @Autowired
    public ExportStarDatasetsStepVerifier(@Qualifier("hikari") JdbcTemplate jdbcTemplate, GZipFile gZipFile) {
        this.jdbcTemplate = jdbcTemplate;
        this.gZipFile = gZipFile;
    }

    @Override
    public Map<Boolean, Optional<String>> verify() {
        // Note: should check database records count if are the same with csv file lines.
        Long totalRecordsOfStarDatasetsTable = jdbcTemplate.queryForObject(SELECT_QUERY, Long.class);

        return (totalRecordsOfStarDatasetsTable != countLinesOfStarDatasetsCsvFile())
                ? Collections.singletonMap(false, Optional.of("##### STARDATASETS BATCH EXTRACTION NOT COMPLETED SUCCESSFULLY #####"))
                : Collections.singletonMap(true, Optional.empty());
    }

    private long countLinesOfStarDatasetsCsvFile() {

        try {
            String fileDirToReadFrom = gZipFile.getFileDirToReadFrom();

            return Files.lines(Paths.get(fileDirToReadFrom)).count();

        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
