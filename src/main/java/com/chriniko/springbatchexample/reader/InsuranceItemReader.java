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

    private int lineNo = 1;
    private final int linesToSkip = 1;

    private BufferedReader bufferedReader;

    public InsuranceItemReader() {

        try {
            lineNo = 1;

            URI uri = Optional
                    .ofNullable(getClass().getClassLoader().getResource("files/FL_insurance_sample.csv"))
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

        String line = bufferedReader.readLine();
        if (line == null || line.isEmpty()) {
            return null;
        }

        String[] splittedData = line.split(",");

        return Insurance.builder()
                .policyId(BigInteger.valueOf(Long.parseLong(splittedData[0])))
                .stateCode(splittedData[1])
                .country(splittedData[2])
                .eqSiteLimit(BigDecimal.valueOf(Double.parseDouble(splittedData[3])))
                .huSiteLimit(BigDecimal.valueOf(Double.parseDouble(splittedData[4])))
                .flSiteLimit(BigDecimal.valueOf(Double.parseDouble(splittedData[5])))
                .frSiteLimit(BigDecimal.valueOf(Double.parseDouble(splittedData[6])))
                .tiv2011(BigDecimal.valueOf(Double.parseDouble(splittedData[7])))
                .tiv2012(BigDecimal.valueOf(Double.parseDouble(splittedData[8])))
                .eqSiteDeductible(BigDecimal.valueOf(Double.parseDouble(splittedData[9])))
                .huSiteDeductible(BigDecimal.valueOf(Double.parseDouble(splittedData[10])))
                .flSiteDeductible(BigDecimal.valueOf(Double.parseDouble(splittedData[11])))
                .frSiteDeductible(BigDecimal.valueOf(Double.parseDouble(splittedData[12])))
                .pointLatitude(BigDecimal.valueOf(Double.parseDouble(splittedData[13])))
                .pointLongitude(BigDecimal.valueOf(Double.parseDouble(splittedData[14])))
                .line(splittedData[15])
                .construction(splittedData[16])
                .pointGranularity(BigInteger.valueOf(Long.parseLong(splittedData[17])))
                .build();
    }
}
