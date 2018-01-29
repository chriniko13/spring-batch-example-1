package com.chriniko.springbatchexample.reader;

import com.chriniko.springbatchexample.domain.StarDataset;
import com.chriniko.springbatchexample.util.GZipFile;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StarDatasetReader implements ItemReader<StarDataset> {

    private static final String SPLIT_DELIM = ",";

    private static final Object __MUTEX = new Object(); // Note: remove synchronization if no task executor used during item reading.

    private BufferedReader bufferedReader;

    @Autowired
    private GZipFile gZipFile;

    @PostConstruct
    public void init() {
        try {
            bufferedReader = Files.newBufferedReader(Paths.get(gZipFile.getFileDirToReadFrom()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StarDataset read() throws Exception {

        String line;
        synchronized (__MUTEX) {
            line = bufferedReader.readLine();
        }

        if (line == null || line.isEmpty()) {
            return null;
        }

        String[] splittedData = line.split(SPLIT_DELIM);

        return StarDataset
                .builder()
                .charge(new BigDecimal(trimmer(splittedData[0])))
                .clus(new BigInteger(trimmer(splittedData[1])))
                .dst(new BigInteger(trimmer(splittedData[2])))
                .enumber(new BigInteger(trimmer(splittedData[3])))
                .etime(new BigDecimal(trimmer(splittedData[4])))
                .hist(new BigDecimal(trimmer(splittedData[5])))
                .nlb(new BigInteger(trimmer(splittedData[6])))
                .qxb(new BigDecimal(trimmer(splittedData[7])))
                .rnumber(new BigDecimal(trimmer(splittedData[8])))
                .tracks(new BigInteger(trimmer(splittedData[9])))
                .vertex(new BigDecimal(trimmer(splittedData[10])))
                .zdc(new BigInteger(trimmer(splittedData[11])))
                .build();
    }

    private String trimmer(String input) {
        return input.trim();
    }
}
