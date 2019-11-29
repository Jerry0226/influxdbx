package com.zsmart.zcm.nms.sync.model;

public class JobExecStats {

    long syncSuccessCount = 0;
    long syncFailCount = 0;

    public long getSyncSuccessCount() {
        return syncSuccessCount;
    }

    public void setSyncSuccessCount(long syncSuccessCount) {
        this.syncSuccessCount = syncSuccessCount;
    }

    public long getSyncFailCount() {
        return syncFailCount;
    }

    public void setSyncFailCount(long syncFailCount) {
        this.syncFailCount = syncFailCount;
    }



}
