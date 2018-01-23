package com.chriniko.springbatchexample.writer;

import com.chriniko.springbatchexample.domain.Insurance;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import javax.sql.DataSource;

public class InsuranceItemWriter extends JdbcBatchItemWriter<Insurance> {


    public InsuranceItemWriter(DataSource dataSource) {
        super();

        this.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        this.setSql("INSERT INTO insurance_tbl (policyId, stateCode, " +
                "country, eqSiteLimit, " +
                "huSiteLimit, flSiteLimit, " +
                "frSiteLimit, tiv2011, " +
                "tiv2012, eqSiteDeductible, " +
                "huSiteDeductible, flSiteDeductible, " +
                "frSiteDeductible, pointLatitude, " +
                "pointLongitude, line, " +
                "construction, pointGranularity, " +
                "processed) " +
                "VALUES (:policyId, :stateCode, " +
                ":country, :eqSiteLimit, " +
                ":huSiteLimit, :flSiteLimit, " +
                ":frSiteLimit, :tiv2011, " +
                ":tiv2012, :eqSiteDeductible, " +
                ":huSiteDeductible, :flSiteDeductible, " +
                ":frSiteDeductible, :pointLatitude, " +
                ":pointLongitude, :line, :construction, " +
                ":pointGranularity, :processed)");
        this.setDataSource(dataSource);
    }

}
