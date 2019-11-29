package com.zsmart.zcm.nms.sync.model;

import org.joda.time.DateTime;

import java.util.Date;

public final class SyncStats {


    DateTime startTime;

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

    DateTime endTime;

    int jobCount = 0;
    long syncSuccessCount = 0;
    long syncFailCount = 0;


    public void addSyncSuccessCount(long syncSuccessCount) {
        this.syncSuccessCount = this.syncSuccessCount + syncSuccessCount;
    }

    public void addSyncFailCount(long syncFailCount) {
        this.syncFailCount = this.syncFailCount + syncFailCount;
    }

    public void setJobCount(int jobCount) {
        this.jobCount = jobCount;
    }

    @Override
    public String toString() {
        return "SyncStats {" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ((endTime != null && startTime != null) ?
                " duration(s): " +
                (endTime.getMillis() - startTime.getMillis())/1000 : "") +
                ", jobCount=" + jobCount +
                ", syncSuccessCount=" + syncSuccessCount +
                ", syncFailCount=" + syncFailCount +
                '}';
    }
}
