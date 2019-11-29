package com.zsmart.zcm.nms.influxdb;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.Serializable;

/**
 * @author chm
 */
public class InfluxDBClientConfig implements Serializable {



    /**
     * 默认参数，必须要有
     */
    String influxDBUrl;

    String influxDBUser;

    String influxDBPassword;

    String database;

    String rp;

    boolean influxdbBatchEnabled = false;

    int influxdbBatchSize = 5000;

    int influxDBBatchPeriod = 3;

    public boolean isInfluxdbBatchEnabled() {
        return this.influxdbBatchEnabled;
    }


    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getRp() {
        return rp;
    }

    public void setRp(String rp) {
        this.rp = rp;
    }

    public void setInfluxDBUrl(String influxDBUrl) {
        this.influxDBUrl = influxDBUrl;
    }

    public void setInfluxDBUser(String influxDBUser) {
        this.influxDBUser = influxDBUser;
    }

    public void setInfluxDBPassword(String influxDBPassword) {
        this.influxDBPassword = influxDBPassword;
    }

    public void setInfluxdbBatchEnabled(boolean influxdbBatchEnabled) {
        this.influxdbBatchEnabled = influxdbBatchEnabled;
    }

    public int getInfluxdbBatchSize() {
        return influxdbBatchSize;
    }

    public void setInfluxdbBatchSize(int influxdbBatchSize) {
        this.influxdbBatchSize = influxdbBatchSize;
    }

    public void setInfluxDBBatchPeriod(int influxDBBatchPeriod) {
        this.influxDBBatchPeriod = influxDBBatchPeriod;
    }


    /**
     * 返回influxdb 的url
     * 
     * @return
     */
    public String getInfluxDBUrl() {
        return this.influxDBUrl;
    }

    public String getInfluxDBUser() {
        return this.influxDBUser;
    }

    public String getInfluxDBPassword() {
        return this.influxDBPassword;
    }

    /**
     * 多少条进行提交
     * 
     * @return
     */
    public int getInfluxDBBatchSize() {
        return this.influxdbBatchSize;
    }

    /**
     * 单位是秒，多久会进行提交
     * 
     * @return
     */
    public int getInfluxDBBatchPeriod() {
        return this.influxDBBatchPeriod;
    }


    public void checkPreCondition() {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(influxDBUrl), "influxDBUrl must not be null or empty.");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(database), "database must not be null or empty.");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(rp), "retention policies must not be null or empty.");
    }


    public static Builder influxDBUrl(final String influxDBUrl, final String influxDBUser, final String influxDBPassword) {
        return new Builder(influxDBUrl, influxDBUser, influxDBPassword);
    }

    public static final class Builder {

        String influxDBUrl;

        String influxDBUser;

        String influxDBPassword;

        boolean influxdbBatchEnabled = true;

        int influxdbBatchSize = 5000;

        int influxDBBatchPeriod = 3;

        String database;

        String rp;


        Builder(final String influxDBUrl, final String influxDBUser, final String influxDBPassword) {


            this.influxDBUrl = influxDBUrl.trim();
            this.influxDBUser = influxDBUser;
            this.influxDBPassword = influxDBPassword;
        }

        public Builder influxdbBatchEnabled(boolean enabled) {
            this.influxdbBatchEnabled = enabled;
            return this;
        }

        public Builder influxdbBatchSize(int influxdbBatchSize) {
            this.influxdbBatchSize = influxdbBatchSize;
            return this;
        }

        public Builder influxDBBatchPeriod(int influxDBBatchPeriod) {
            this.influxDBBatchPeriod = influxDBBatchPeriod;
            return this;
        }


        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder rp(String rp) {
            this.rp = rp;
            return this;
        }


        /**
         * build进行构造
         * @return
         */
        public InfluxDBClientConfig build() {

            Preconditions.checkArgument(!Strings.isNullOrEmpty(influxDBUrl), "influxDBUrl must not be null or empty.");


            InfluxDBClientConfig config = new InfluxDBClientConfig();
            config.influxdbBatchEnabled = this.influxdbBatchEnabled;
            config.influxDBBatchPeriod = this.influxDBBatchPeriod;
            config.influxdbBatchSize = this.influxdbBatchSize;
            config.influxDBUrl = this.influxDBUrl;
            config.influxDBUser = this.influxDBUser;
            config.influxDBPassword = this.influxDBPassword;
            config.database = this.database;
            config.rp = this.rp;

            return config;

        }

    }

    

}
