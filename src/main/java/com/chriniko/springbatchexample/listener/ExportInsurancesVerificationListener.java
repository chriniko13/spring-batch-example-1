package com.chriniko.springbatchexample.listener;

import com.chriniko.springbatchexample.event.JobFinishedEvent;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ExportInsurancesVerificationListener implements JobExecutionListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    private final JdbcTemplate jdbcTemplate;

    public ExportInsurancesVerificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Do nothing.
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            // Note: should check database records count if are the same with (csv file lines - 1).

            jdbcTemplate.query(
                    "SELECT count(*) FROM spring_batch_example.insurance_tbl",
                    new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rs) throws SQLException {

                            long totalRecordsOfInsuranceTable = rs.getLong(1);

                            if (totalRecordsOfInsuranceTable != countLinesOfInsurancesCsvFile()) {

                                System.err.println("##### INSURANCES BATCH EXTRACTION NOT COMPLETED SUCCESSFULLY #####");
                                throw new IllegalStateException("not correct insurances batch operation!");

                            } else {
                                System.out.println("##### INSURANCES BATCH EXTRACTION COMPLETED SUCCESSFULLY #####");
                            }
                        }
                    });

            applicationEventPublisher.publishEvent(new JobFinishedEvent(this, jobExecution.getJobConfigurationName()));
        }
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

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
