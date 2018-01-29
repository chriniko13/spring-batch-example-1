package com.chriniko.springbatchexample.reader;

import com.chriniko.springbatchexample.domain.Insurance;
import org.springframework.batch.item.ItemReader;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class InsuranceItemReader implements ItemReader<Insurance> {

    private static final String SPLIT_DELIM = ",";

    private static final String FILE_INSURANCE_SAMPLE_CSV = "files/FL_insurance_sample.csv";

    private static final Object __MUTEX = new Object(); // Note: remove synchronization if no task executor used during item reading.

    private int lineNo = 1;
    private final int linesToSkip = 1;

    private BufferedReader bufferedReader;

    public InsuranceItemReader() {

        try {
            URI uri = Optional
                    .ofNullable(getClass().getClassLoader().getResource(FILE_INSURANCE_SAMPLE_CSV))
                    .orElseThrow(IllegalStateException::new)
                    .toURI();

            bufferedReader = Files.newBufferedReader(Paths.get(uri));

            while (lineNo++ <= linesToSkip) {
                bufferedReader.readLine(); // Note: toss-skip line.
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Insurance read() throws Exception {

        String line;
        synchronized (__MUTEX) {
            line = bufferedReader.readLine();
        }

        if (line == null || line.isEmpty()) {
            return null;
        }

        String[] splittedData = line.split(SPLIT_DELIM);

        return Insurance.builder()
                .policyId(new BigInteger(splittedData[0]))
                .stateCode(splittedData[1])
                .country(splittedData[2])
                .eqSiteLimit(new BigDecimal(splittedData[3]))
                .huSiteLimit(new BigDecimal(splittedData[4]))
                .flSiteLimit(new BigDecimal(splittedData[5]))
                .frSiteLimit(new BigDecimal(splittedData[6]))
                .tiv2011(new BigDecimal(splittedData[7]))
                .tiv2012(new BigDecimal(splittedData[8]))
                .eqSiteDeductible(new BigDecimal(splittedData[9]))
                .huSiteDeductible(new BigDecimal(splittedData[10]))
                .flSiteDeductible(new BigDecimal(splittedData[11]))
                .frSiteDeductible(new BigDecimal(splittedData[12]))
                .pointLatitude(new BigDecimal(splittedData[13]))
                .pointLongitude(new BigDecimal(splittedData[14]))
                .line(splittedData[15])
                .construction(splittedData[16])
                .pointGranularity(new BigInteger(splittedData[17]))
                .build();
    }
}
