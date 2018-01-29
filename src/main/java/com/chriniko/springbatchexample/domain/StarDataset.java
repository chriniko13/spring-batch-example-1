package com.chriniko.springbatchexample.domain;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StarDataset {

    /*
        Note:

        charge  FLOAT
        clus    INT
        dst     INT
        enumber INT
        etime   DOUBLE
        hist    INT
        nlb     INT
        qxb     FLOAT
        rnumber INT
        tracks  INT
        vertex  FLOAT
        zdc     INT
     */

    private BigDecimal charge;
    private BigInteger clus;
    private BigInteger dst;
    private BigInteger enumber;
    private BigDecimal etime;
    private BigDecimal hist;
    private BigInteger nlb;
    private BigDecimal qxb;
    private BigDecimal rnumber;
    private BigInteger tracks;
    private BigDecimal vertex;
    private BigInteger zdc;

    private boolean processed;

}
