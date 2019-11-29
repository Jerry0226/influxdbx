package com.zsmart.zcm.nms.sync.model;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zsmart.zcm.nms.influxdb.InfluxDBClientConfig;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * @author  chm
 */
public class SyncProperties implements Serializable {


    public int getJobTaskCount() {
        return jobTaskCount;
    }

    public void setJobTaskCount(int jobTaskCount) {
        this.jobTaskCount = jobTaskCount;
    }

    /**
     * 并发的任务数
     */
    private int jobTaskCount = 10;

    /**
     * InfluxDBClientConfig
     */
    private InfluxDBClientConfig sourceClient;

    /**
     * InfluxDBClientConfig
     */
    private InfluxDBClientConfig destClient;

    /**
     * 同步时数据切分的大小，考虑到数据会很大；把大的时间段切分为小的，然后启动更多的任务进行并发同步动作，提高效率
     * 单位秒
     */
    private int splitIntervalS = 10 ;


    /**
     * 毫秒
     */
    private DateTime startTime;


    /**
     * 毫秒
     */
    private DateTime endTime;

    /**
     * 待同步的表
     */
    private String measurement;



    public String getMeasurement() {
        return measurement;
    }


    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }


    public InfluxDBClientConfig getSourceClient() {
        return sourceClient;
    }

    public void setSourceClient(InfluxDBClientConfig sourceClient) {
        this.sourceClient = sourceClient;
    }

    public InfluxDBClientConfig getDestClient() {
        return destClient;
    }

    public void setDestClient(InfluxDBClientConfig destClient) {
        this.destClient = destClient;
    }

    public int getSplitIntervalS() {
        return splitIntervalS;
    }

    public void setSplitIntervalS(int splitIntervalS) {
        this.splitIntervalS = splitIntervalS;
    }

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


    public void checkPreCondition() {

        this.sourceClient.checkPreCondition();
        this.destClient.checkPreCondition();


        Preconditions.checkArgument(!Strings.isNullOrEmpty(measurement), "measurement must not be null or empty.");

        Preconditions.checkArgument(startTime != null || endTime != null,
                "StartTime and EndTime must not be null or empty.");

        Preconditions.checkArgument(startTime.isBefore(endTime), "StartTime must be before EndTime.");


    }

}
