package com.chriniko.springbatchexample.configuration;

import com.chriniko.springbatchexample.domain.Insurance;
import com.chriniko.springbatchexample.domain.StarDataset;
import com.chriniko.springbatchexample.listener.ExportInsurancesVerificationListener;
import com.chriniko.springbatchexample.listener.PerformanceLoggingStepExecutionListener;
import com.chriniko.springbatchexample.processor.InsuranceItemProcessor;
import com.chriniko.springbatchexample.processor.StarDatasetItemProcessor;
import com.chriniko.springbatchexample.reader.InsuranceItemReader;
import com.chriniko.springbatchexample.reader.StarDatasetReader;
import com.chriniko.springbatchexample.writer.InsuranceItemWriter;
import com.chriniko.springbatchexample.writer.StarDatasetItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Value("${max.threads.for.insurances.step}")
    private int maxThreadsForInsurances;

    @Value("${insurances.chunk.size}")
    private int insurancesChunkSize;

    //TODO USE THEM SOMEHOW IN THIS EXAMPLE...
//    a JobRepository (bean name "jobRepository")
//    a JobLauncher (bean name "jobLauncher")
//    a JobRegistry (bean name "jobRegistry")
//    a org.springframework.batch.core.launch.JobOperator (bean name "jobOperator")
//    a org.springframework.batch.core.explore.JobExplorer (bean name "jobExplorer")
//    a PlatformTransactionManager (bean name "transactionManager")


    @Bean
    public Job job(@Qualifier("hikari") @Autowired DataSource dataSource,
                   @Autowired TaskExecutor taskExecutor,
                   @Qualifier("hikari") @Autowired JdbcTemplate jdbcTemplate) {
        return jobs.get("exportInsurances")
                .incrementer(new RunIdIncrementer())
                .flow(insurancesFromCsvToDbStep(dataSource, taskExecutor))
                .next(starDatasetsFromCsvToDbStep(taskExecutor))
                //TODO add one more step...
                .end()
                .listener(exportInsurancesVerificationListener(jdbcTemplate)) //TODO rename this listener to a proper name...
                .build();
    }


    // ----------------------- START: begin of step declaration -----------------------------
    @Bean
    protected Step insurancesFromCsvToDbStep(DataSource dataSource, TaskExecutor taskExecutor) {
        return steps
                .get("insurancesFromCsvToDbStep")
                .<Insurance, Insurance>chunk(insurancesChunkSize)
                .reader(insuranceItemReader())
                .processor(insuranceItemProcessor())
                .writer(insuranceJdbcBatchItemWriter(dataSource))
                .listener(performanceLoggingStepExecutionListener())
                //TODO add more listeners...
                .taskExecutor(taskExecutor)
                .throttleLimit(maxThreadsForInsurances)
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
    public JdbcBatchItemWriter<Insurance> insuranceJdbcBatchItemWriter(@Qualifier("hikari")
                                                                       @Autowired DataSource dataSource) {
        return new InsuranceItemWriter(dataSource);
    }
    // -------------------------- END: begin of step declaration -----------------------------


    // ----------------------- START: begin of step declaration -----------------------------
    @Bean
    public Step starDatasetsFromCsvToDbStep(TaskExecutor taskExecutor) {
        return steps
                .get("starDatasetsFromCsvToDbStep")
                .<StarDataset, StarDataset>chunk(300) //TODO extract it to value
                .reader(starDatasetReader())
                .processor(starDatasetItemProcessor())
                .writer(starDatasetItemWriter())
                .listener(performanceLoggingStepExecutionListener())
                //TODO add more listeners...
                .taskExecutor(taskExecutor)
                .throttleLimit(70) //TODO extract it to value
                .build();
    }

    @Bean
    public StarDatasetReader starDatasetReader() {
        return new StarDatasetReader();
    }

    @Bean
    public StarDatasetItemProcessor starDatasetItemProcessor() {
        return new StarDatasetItemProcessor();
    }

    @Bean
    public StarDatasetItemWriter starDatasetItemWriter() {
        return new StarDatasetItemWriter();
    }
    // ------------------------- END: begin of step declaration -----------------------------


    // ----------------------- START: begin of util for steps -----------------------------
    @Bean
    public ExportInsurancesVerificationListener exportInsurancesVerificationListener(@Qualifier("hikari")
                                                                                     @Autowired JdbcTemplate jdbcTemplate) {
        return new ExportInsurancesVerificationListener(jdbcTemplate);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PerformanceLoggingStepExecutionListener performanceLoggingStepExecutionListener() {
        return new PerformanceLoggingStepExecutionListener();
    }
    // ------------------------- END: begin of util for steps -----------------------------

}