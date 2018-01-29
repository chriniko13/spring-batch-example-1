package com.chriniko.springbatchexample.writer;

import com.chriniko.springbatchexample.domain.StarDataset;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class StarDatasetItemWriter implements ItemWriter<StarDataset> {

    private static final String INSERT_QUERY = "INSERT INTO spring_batch_example.star_dataset_tbl (charge, clus, dst, enumber, etime, hist, nlb, qxb, rnumber, tracks, vertex, zdc, processed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Qualifier("hikari")
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    @Qualifier("hikari")
    private JdbcTemplate jdbcTemplate;

    @Override
    public void write(final List<? extends StarDataset> starDatasets) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {


                jdbcTemplate.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {

                        final StarDataset starDataset = starDatasets.get(i);

                        ps.setBigDecimal(1, starDataset.getCharge());
                        ps.setObject(2, starDataset.getClus(), Types.BIGINT);
                        ps.setObject(3, starDataset.getDst(), Types.BIGINT);
                        ps.setObject(4, starDataset.getEnumber(), Types.BIGINT);
                        ps.setBigDecimal(5, starDataset.getEtime());
                        ps.setBigDecimal(6, starDataset.getHist());
                        ps.setObject(7, starDataset.getNlb(), Types.BIGINT);
                        ps.setBigDecimal(8, starDataset.getQxb());
                        ps.setBigDecimal(9, starDataset.getRnumber());
                        ps.setObject(10, starDataset.getTracks(), Types.BIGINT);
                        ps.setBigDecimal(11, starDataset.getVertex());
                        ps.setObject(12, starDataset.getZdc(), Types.BIGINT);
                        ps.setBoolean(13, starDataset.isProcessed());
                    }

                    @Override
                    public int getBatchSize() {
                        return starDatasets.size();
                    }

                });

            }
        });

    }
}
