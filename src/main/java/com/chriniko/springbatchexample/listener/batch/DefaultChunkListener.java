package com.chriniko.springbatchexample.listener.batch;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class DefaultChunkListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext context) {
        System.out.println("DefaultChunkListener#beforeChunk");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        System.out.println("DefaultChunkListener#afterChunk");

    }

    @Override
    public void afterChunkError(ChunkContext context) {
        System.out.println("DefaultChunkListener#afterChunkError");
    }
}
