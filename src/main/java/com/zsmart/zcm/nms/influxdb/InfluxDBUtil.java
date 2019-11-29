package com.zsmart.zcm.nms.influxdb;

import com.google.common.base.Strings;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.QueryResult;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class InfluxDBUtil {



    private InfluxDBUtil() {
    }

    /**
     * 获取influxDB
     *
     * @return InfluxDB
     */
    public static InfluxDB getInfluxDB(InfluxDBClientConfig config) {

        InfluxDB influxDB = null;
        String influxDBUrl = config.getInfluxDBUrl();
        String influxDBUser = config.getInfluxDBUser();
        String influxDBPassword = config.getInfluxDBPassword();
        if (!Strings.isNullOrEmpty(influxDBUser) && !Strings.isNullOrEmpty(influxDBPassword)) {
            influxDB = InfluxDBFactory.connect(influxDBUrl, influxDBUser, influxDBPassword);
        }
        else {
            influxDB = InfluxDBFactory.connect(influxDBUrl);
        }
        // 改为批量模式
        influxDB.enableBatch(config.influxdbBatchSize, config.influxDBBatchPeriod, TimeUnit.SECONDS);
        return influxDB;
    }


    /**
     * 获取result的值
     * @param queryResult
     * @return
     */
    public static QueryResult.Result getResult(QueryResult queryResult) {
        List<QueryResult.Result> resultList = queryResult.getResults();
        if (!CollectionUtils.isEmpty(resultList)) {
            QueryResult.Result result = resultList.get(0);
            List<QueryResult.Series> seriesList = result.getSeries();
            if(!CollectionUtils.isEmpty(seriesList)) {
                return result;
            }
        }
        return null;
    }





}
