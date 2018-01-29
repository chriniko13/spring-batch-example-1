package com.chriniko.springbatchexample.processor;

import com.chriniko.springbatchexample.domain.StarDataset;
import org.springframework.batch.item.ItemProcessor;

public class StarDatasetItemProcessor implements ItemProcessor<StarDataset, StarDataset> {

    private static final boolean LOG_WHEN_PROCESSING = false;

    @Override
    public StarDataset process(StarDataset starDataset) {
        starDataset.setProcessed(true);

        if (LOG_WHEN_PROCESSING)
            System.out.println("[StarDatasetItemProcessor#process] starDataset = " + starDataset);

        return starDataset;
    }
}
