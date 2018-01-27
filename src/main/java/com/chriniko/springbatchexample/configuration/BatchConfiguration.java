package com.chriniko.springbatchexample.configuration;

import com.chriniko.springbatchexample.domain.Insurance;
import com.chriniko.springbatchexample.listener.ExportInsurancesVerificationListener;
import com.chriniko.springbatchexample.listener.PerformanceLoggingStepExecutionListener;
import com.chriniko.springbatchexample.processor.InsuranceItemProcessor;
import com.chriniko.springbatchexample.reader.InsuranceItemReader;
import com.chriniko.springbatchexample.writer.InsuranceItemWriter;
import com.zaxxer.hikari.HikariDataSource;
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
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Value("${max.threads.for.insurances.step}")
    private int maxThreads;

    //TODO USE THEM SOMEHOW IN THIS EXAMPLE...
//    a JobRepository (bean name "jobRepository")
//    a JobLauncher (bean name "jobLauncher")
//    a JobRegistry (bean name "jobRegistry")
//    a org.springframework.batch.core.launch.JobOperator (bean name "jobOperator")
//    a org.springframework.batch.core.explore.JobExplorer (bean name "jobExplorer")
//    a PlatformTransactionManager (bean name "transactionManager")

    @Bean
    public CountDownLatch countDownLatchForTaskExecutor() {
        return new CountDownLatch(1); // Note: the number should be equal to the number of the jobs that spring batch has to execute.
    }

    @Bean
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(50);
        taskExecutor.setMaxPoolSize(100);

        taskExecutor.setAllowCoreThreadTimeOut(false);
        taskExecutor.setKeepAliveSeconds(7);

        taskExecutor.setQueueCapacity(0);

        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadNamePrefix("spring-batch-worker");

        return taskExecutor;
    }

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
                //TODO add one more step...
                .end()
                .listener(exportInsurancesVerificationListener()) //TODO rename this listener to a proper name...
                .build();
    }


    // ----------------------- START: begin of step declaration -----------------------------
    @Bean
    protected Step insurancesFromCsvToDbStep() {
        return steps
                .get("insurancesFromCsvToDbStep")
                .<Insurance, Insurance>chunk(200)
                .reader(insuranceItemReader())
                .processor(insuranceItemProcessor())
                .writer(insuranceJdbcBatchItemWriter())
                .listener(performanceLoggingStepExecutionListener())
                //TODO add more listeners...
                .taskExecutor(taskExecutor())
                .throttleLimit(maxThreads)
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
    // -------------------------- END: begin of step declaration -----------------------------


    // ----------------------- START: begin of step declaration -----------------------------
    // ------------------------- END: begin of step declaration -----------------------------


    // ----------------------- START: begin of util for steps -----------------------------
    @Bean
    public ExportInsurancesVerificationListener exportInsurancesVerificationListener() {
        return new ExportInsurancesVerificationListener(jdbcTemplate());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PerformanceLoggingStepExecutionListener performanceLoggingStepExecutionListener() {
        return new PerformanceLoggingStepExecutionListener();
    }
    // ------------------------- END: begin of util for steps -----------------------------

}
