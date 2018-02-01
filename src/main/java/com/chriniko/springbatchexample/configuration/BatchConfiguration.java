package com.chriniko.springbatchexample.configuration;

import com.chriniko.springbatchexample.domain.Insurance;
import com.chriniko.springbatchexample.domain.StarDataset;
import com.chriniko.springbatchexample.listener.batch.JobVerificationListener;
import com.chriniko.springbatchexample.listener.batch.PerformanceLoggingStepExecutionListener;
import com.chriniko.springbatchexample.processor.InsuranceItemProcessor;
import com.chriniko.springbatchexample.processor.StarDatasetItemProcessor;
import com.chriniko.springbatchexample.reader.InsuranceItemReader;
import com.chriniko.springbatchexample.reader.StarDatasetReader;
import com.chriniko.springbatchexample.task.ErroneousTasklet;
import com.chriniko.springbatchexample.writer.InsuranceItemWriter;
import com.chriniko.springbatchexample.writer.StarDatasetItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private TaskExecutor taskExecutor;

    @Value("${max.threads.for.insurances.step}")
    private int maxThreadsForInsurances;

    @Value("${insurances.chunk.size}")
    private int insurancesChunkSize;

    @Value("${max.threads.for.stardatasets.step}")
    private int maxThreadForDatasets;

    @Value("${stardatasets.chunk.size}")
    private int starDatasetsChunkSize;

//    a PlatformTransactionManager (bean name "transactionManager") TODO add it...

    @Bean
    public Job job() {
        final String exportJob_Name = "exportJob#3";

        return jobs.get(exportJob_Name)
                .incrementer(new RunIdIncrementer())
                .flow(insurancesFromCsvToDbStep(taskExecutor))
                .next(sampleTaskletStep(taskExecutor))
                .next(starDatasetsFromCsvToDbStep(taskExecutor))
                .end()
                .listener(jobVerificationListener())
                .build();
    }


    // ----------------------- START: begin of step declaration -----------------------------
    @Bean
    protected Step insurancesFromCsvToDbStep(TaskExecutor taskExecutor) {
        final String insurancesFromCsvToDbStep = "insurancesFromCsvToDbStep";

        return steps
                .get(insurancesFromCsvToDbStep)
                .<Insurance, Insurance>chunk(insurancesChunkSize)
                .reader(insuranceItemReader())
                .processor(insuranceItemProcessor())
                .writer(insuranceJdbcBatchItemWriter())
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
    public InsuranceItemWriter insuranceJdbcBatchItemWriter() {
        return new InsuranceItemWriter();
    }
    // -------------------------- END: begin of step declaration -----------------------------


    // ----------------------- START: begin of step declaration -----------------------------
    @Bean
    public Step starDatasetsFromCsvToDbStep(TaskExecutor taskExecutor) {
        final String starDatasetsFromCsvToDbStep = "starDatasetsFromCsvToDbStep";

        return steps
                .get(starDatasetsFromCsvToDbStep)
                .<StarDataset, StarDataset>chunk(maxThreadForDatasets)
                .reader(starDatasetReader())
                .processor(starDatasetItemProcessor())
                .writer(starDatasetItemWriter())
                .listener(performanceLoggingStepExecutionListener())
                //TODO add more listeners...
                .taskExecutor(taskExecutor)
                .throttleLimit(starDatasetsChunkSize)
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


    // ----------------------- START: begin of step declaration -----------------------------
    @Autowired
    private ErroneousTasklet erroneousTasklet;

    @Bean
    public Step sampleTaskletStep(TaskExecutor taskExecutor) {
        final String sampleTaskletStep = "sampleTaskletStep";

        return steps
                .get(sampleTaskletStep)
                .tasklet(erroneousTasklet)
                .listener(performanceLoggingStepExecutionListener())
                .taskExecutor(taskExecutor)
                .throttleLimit(1) //TODO extract it to config. value.
                .build();
    }
    // ------------------------- END: begin of step declaration -----------------------------


    // ----------------------- START: begin of util for steps -----------------------------
    @Bean
    public JobVerificationListener jobVerificationListener() {
        return new JobVerificationListener();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PerformanceLoggingStepExecutionListener performanceLoggingStepExecutionListener() {
        return new PerformanceLoggingStepExecutionListener();
    }
    // ------------------------- END: begin of util for steps -----------------------------

}
