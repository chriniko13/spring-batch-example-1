package com.chriniko.springbatchexample.processor;

import com.chriniko.springbatchexample.domain.Insurance;
import org.springframework.batch.item.ItemProcessor;


public class InsuranceItemProcessor implements ItemProcessor<Insurance, Insurance> {

    @Override
    public Insurance process(Insurance insurance) {

        insurance.setProcessed(true);

        System.out.println("[InsuranceItemProcessor#process] insurance = " + insurance);

        return insurance;
    }
}
