package com.chriniko.springbatchexample.configuration;

import com.chriniko.springbatchexample.domain.Insurance;
import com.chriniko.springbatchexample.listener.ExportInsurancesVerificationListener;
import com.chriniko.springbatchexample.processor.InsuranceItemProcessor;
import com.chriniko.springbatchexample.reader.InsuranceItemReader;
import com.chriniko.springbatchexample.writer.InsuranceItemWriter;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLauncher jobLauncher;

    //TODO USE THEM...
//    a JobRepository (bean name "jobRepository")
//    a JobLauncher (bean name "jobLauncher")
//    a JobRegistry (bean name "jobRegistry")
//    a org.springframework.batch.core.launch.JobOperator (bean name "jobOperator")
//    a org.springframework.batch.core.explore.JobExplorer (bean name "jobExplorer")
//    a PlatformTransactionManager (bean name "transactionManager")

    @Qualifier("hikari")
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        jdbcTemplate.setDataSource(dataSource());

        return jdbcTemplate;
    }

    @Qualifier("hikari")
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost/spring_batch_example?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("nikos");

        return dataSource;
    }

    @Bean
    public Job job() {
        return jobs.get("exportInsurances")
                .incrementer(new RunIdIncrementer())
                .flow(insurancesFromCsvToDbStep())
                .end()
                .listener(exportInsurancesVerificationListener())
                .build();
    }

    @Bean
    protected Step insurancesFromCsvToDbStep() {
        return steps
                .get("insurancesFromCsvToDbStep")
                .<Insurance, Insurance>chunk(200)
                .reader(insuranceItemReader())
                .processor(insuranceItemProcessor())
                .writer(insuranceJdbcBatchItemWriter())
                .build();
    }

    @Bean
    public InsuranceItemReader insuranceItemReader() {
        return new InsuranceItemReader();
    }

    @Bean
    public InsuranceItemProcessor insuranceItemProcessor() {
        return new InsuranceItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Insurance> insuranceJdbcBatchItemWriter() {
        return new InsuranceItemWriter(dataSource());
    }

    @Bean
    @Scope("prototype")
    public ExportInsurancesVerificationListener exportInsurancesVerificationListener() {
        return new ExportInsurancesVerificationListener(jdbcTemplate());
    }

}
