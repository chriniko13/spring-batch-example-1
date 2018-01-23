package com.chriniko.springbatchexample.domain;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Insurance {

    private BigInteger policyId;

    private String stateCode;

    private String country;

    private BigDecimal eqSiteLimit;
    private BigDecimal huSiteLimit;
    private BigDecimal flSiteLimit;
    private BigDecimal frSiteLimit;

    private BigDecimal tiv2011;
    private BigDecimal tiv2012;

    private BigDecimal eqSiteDeductible;
    private BigDecimal huSiteDeductible;
    private BigDecimal flSiteDeductible;
    private BigDecimal frSiteDeductible;

    private BigDecimal pointLatitude;
    private BigDecimal pointLongitude;

    private String line;
    private String construction;
    private BigInteger pointGranularity;

    private boolean processed;
}
