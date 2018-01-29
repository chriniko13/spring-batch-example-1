package com.chriniko.springbatchexample.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

@Component
public class GZipFile {

    private static final String STAR2000_CSV_GZ = "files/star2000.csv.gz";
    private static final String STAR2000_CSV = "star2000.csv";

    @Value("${output.directory.star2000}")
    private String outputFileDir;

    private InputStream inputGzipFile;
    private String outputFile;

    @PostConstruct
    void init() {
        inputGzipFile = this.getClass().getClassLoader().getResourceAsStream(STAR2000_CSV_GZ);
        outputFile = outputFileDir + STAR2000_CSV;
        unzip();
    }

    public String getFileDirToReadFrom() {
        return outputFile;
    }

    private void unzip() {

        try (GZIPInputStream gzis =
                     new GZIPInputStream(inputGzipFile);
             FileOutputStream out =
                     new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[1024];

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
