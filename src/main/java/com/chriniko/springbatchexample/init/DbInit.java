package com.chriniko.springbatchexample.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DbInit {

    @Qualifier("hikari")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    void init() {
        //drop database if exists...
        jdbcTemplate.execute("DROP DATABASE IF EXISTS spring_batch_example");

        //create database if not exists...
        jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS  spring_batch_example");

        //use database...
        jdbcTemplate.execute("USE spring_batch_example");

        //drop table if exists...
        jdbcTemplate.execute("DROP TABLE IF EXISTS spring_batch_example.insurance_tbl");

        //create table if not exists...
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS spring_batch_example.insurance_tbl\n" +
                "(\n" +
                "  policyId         BIGINT       NOT NULL\n" +
                "    PRIMARY KEY,\n" +
                "  stateCode        VARCHAR(255) NULL,\n" +
                "  country          VARCHAR(255) NULL,\n" +
                "  eqSiteLimit      DECIMAL      NULL,\n" +
                "  huSiteLimit      DECIMAL      NULL,\n" +
                "  flSiteLimit      DECIMAL      NULL,\n" +
                "  frSiteLimit      DECIMAL      NULL,\n" +
                "  tiv2011          DECIMAL      NULL,\n" +
                "  tiv2012          DECIMAL      NULL,\n" +
                "  eqSiteDeductible DECIMAL      NULL,\n" +
                "  huSiteDeductible DECIMAL      NULL,\n" +
                "  flSiteDeductible DECIMAL      NULL,\n" +
                "  frSiteDeductible DECIMAL      NULL,\n" +
                "  pointLatitude    DECIMAL      NULL,\n" +
                "  pointLongitude   DECIMAL      NULL,\n" +
                "  line             VARCHAR(255) NULL,\n" +
                "  construction     VARCHAR(255) NULL,\n" +
                "  pointGranularity BIGINT       NULL,\n" +
                "  processed        TINYINT(1)   NULL\n" +
                ")\n" +
                "  ENGINE = InnoDB;");
    }

}
