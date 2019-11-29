package com.zsmart.zcm.nms.sample;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zsmart.zcm.nms.influxdb.InfluxDBClientConfig;
import com.zsmart.zcm.nms.sync.SyncInfluxdb;
import com.zsmart.zcm.nms.sync.model.SyncProperties;
import org.joda.time.DateTime;


public class JobTaskSample {


    public static void main(String[] args) {


        SyncProperties syncProperties = new SyncProperties();

        InfluxDBClientConfig sourceConfig = InfluxDBClientConfig
                .influxDBUrl("http://172.16.24.69:8587/", "admin", "admin")
                .database("host_metrics")
                .rp("k2h_raw").build();

        syncProperties.setSourceClient(sourceConfig);


        InfluxDBClientConfig destConfig = InfluxDBClientConfig
                .influxDBUrl("http://172.16.24.70:8586/", "admin", "admin")
                .database("host_metrics")
                .rp("k2h_raw")
                .influxdbBatchEnabled(true)
                .influxDBBatchPeriod(3)
                .influxdbBatchSize(8000).build();

        syncProperties.setDestClient(destConfig);

        syncProperties.setSplitIntervalS(10);


        syncProperties.setMeasurement("cpu");

        DateTime endTime = DateTime.now();

        syncProperties.setEndTime(endTime);
        DateTime startTime = endTime.withMillis(endTime.getMillis() - 2 * 60 * 60 * 1000);
        syncProperties.setStartTime(startTime);

        System.out.println(JSON.toJSONString(syncProperties, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat));


        SyncInfluxdb syncInfluxdb = new SyncInfluxdb();

        syncInfluxdb.sync(syncProperties);

    }

}
