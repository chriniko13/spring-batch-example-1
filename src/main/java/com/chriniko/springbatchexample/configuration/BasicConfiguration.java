package com.chriniko.springbatchexample.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class BasicConfiguration {

    @Bean
    public CountDownLatch countDownLatchForTaskExecutor() {
        return new CountDownLatch(1); // Note: the number should be equal to the number of the jobs that spring batch has to execute.
    }

    @Bean
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(100);
        taskExecutor.setMaxPoolSize(200);

        taskExecutor.setAllowCoreThreadTimeOut(false);
        taskExecutor.setKeepAliveSeconds(15);

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

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(140);
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("nikos");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost/spring_batch_example?useSSL=false");
        hikariConfig.setConnectionTimeout(30000);

        return new HikariDataSource(hikariConfig);
    }

    @Qualifier("hikari")
    @Bean
    public TransactionTemplate transactionTemplate() {

        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
        dataSourceTransactionManager.setDefaultTimeout(30);

        TransactionTemplate transactionTemplate = new TransactionTemplate(dataSourceTransactionManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.setReadOnly(false);
        transactionTemplate.setTimeout(30);

        return transactionTemplate;
    }
}
