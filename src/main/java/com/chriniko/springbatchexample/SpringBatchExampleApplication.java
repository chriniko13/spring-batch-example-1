package com.chriniko.springbatchexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchExampleApplication {


    public static void main(String[] args) {

        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> System.out.println("Exiting JVM...")));

        SpringApplication.run(SpringBatchExampleApplication.class, args);

    }
}
