package com.zsmart.zcm.nms.sync.model;

import org.influxdb.InfluxDB;
import org.joda.time.DateTime;

import java.util.List;

public class JobTaskDto {

    /**
     * 起始时间
     */
    DateTime startTime;

    /**
     * 结束时间
     */
    DateTime endTime;

    /**
     * 源influxdb
     */
    InfluxDB sourceInfluxDB;

    String sourceRP;


    /**
     * 目标influxdb
     */
    InfluxDB destInfluxDB;


    /**
     * 目标存储策略
     */
    String destRP;


    /**
     * 待同步的表
     */
    String measurement;

    /**
     * 表所在的库
     */
    String sourceDatabase;


    /**
     * 目标数据库
     */
    String destDatabase;


    /**
     * 待同步表的tags记录
     */
    List<String> tags;

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public InfluxDB getSourceInfluxDB() {
        return sourceInfluxDB;
    }

    public void setSourceInfluxDB(InfluxDB sourceInfluxDB) {
        this.sourceInfluxDB = sourceInfluxDB;
    }

    public String getSourceRP() {
        return sourceRP;
    }

    public void setSourceRP(String sourceRP) {
        this.sourceRP = sourceRP;
    }

    public InfluxDB getDestInfluxDB() {
        return destInfluxDB;
    }

    public void setDestInfluxDB(InfluxDB destInfluxDB) {
        this.destInfluxDB = destInfluxDB;
    }

    public String getDestRP() {
        return destRP;
    }

    public void setDestRP(String destRP) {
        this.destRP = destRP;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getSourceDatabase() {
        return sourceDatabase;
    }

    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    public String getDestDatabase() {
        return destDatabase;
    }

    public void setDestDatabase(String destDatabase) {
        this.destDatabase = destDatabase;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }



    public JobTaskDto(SyncProperties syncProperties, InfluxDB sourceInfluxDB, InfluxDB destInfluxDB) {

        this.setStartTime(syncProperties.getStartTime());
        this.setEndTime(syncProperties.getEndTime());
        this.setMeasurement(syncProperties.getMeasurement());


        this.setDestDatabase(syncProperties.getDestClient().getDatabase());
        this.setDestInfluxDB(destInfluxDB);
        this.setDestRP(syncProperties.getDestClient().getRp());


        this.setSourceDatabase(syncProperties.getSourceClient().getDatabase());
        this.setSourceInfluxDB(sourceInfluxDB);
        this.setSourceRP(syncProperties.getDestClient().getRp());
    }




}
