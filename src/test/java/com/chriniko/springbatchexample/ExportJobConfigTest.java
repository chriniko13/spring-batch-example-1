package com.chriniko.springbatchexample;

import com.chriniko.springbatchexample.configuration.BatchConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)

@ContextConfiguration(
        classes = {
                SpringBatchExampleApplication.class,
                BatchTestConfiguration.class
        }
)

@TestPropertySource(
        locations = "classpath:application.properties"
)
public class ExportJobConfigTest {

    @Autowired
    private JobLauncherTestUtils testUtils;

    @Autowired
    private BatchConfiguration batchConfiguration;

    @Test
    public void testEntireJob() throws Exception {
        final JobExecution result = testUtils.getJobLauncher().run(batchConfiguration.job(), testUtils.getUniqueJobParameters());
        Assert.assertNotNull(result);
        Assert.assertEquals(BatchStatus.COMPLETED, result.getStatus());
    }

    @Test
    public void testSpecificStep() {
        Assert.assertEquals(BatchStatus.COMPLETED, testUtils.launchStep("insurancesFromCsvToDbStep").getStatus());
    }

}
