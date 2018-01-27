package com.chriniko.springbatchexample.processor;

import com.chriniko.springbatchexample.domain.Insurance;
import org.springframework.batch.item.ItemProcessor;


public class InsuranceItemProcessor implements ItemProcessor<Insurance, Insurance> {

    private static final boolean LOG_WHEN_PROCESSING = false;

    @Override
    public Insurance process(Insurance insurance) {

        insurance.setProcessed(true);

        if (LOG_WHEN_PROCESSING)
            System.out.println("[InsuranceItemProcessor#process] insurance = " + insurance);

        return insurance;
    }
}
