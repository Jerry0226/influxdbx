package com.zsmart.zcm.nms.influxdb;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * sql
 */
public final class InfluxDBSelectCommand {

    private InfluxDBSelectCommand() {
    }

    /**
     * 将本地时间转换为UTC时间字符串
     *
     * @param dateTime String
     * @return String
     */
    public static String convertLocalToUTC(DateTime dateTime) {

        return dateTime.withZone(DateTimeZone.UTC).toString(DateTimeFormat.forPattern(DATETIME_FORMAT_1));
    }

    public static final String DATETIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";


    public static Builder SelectCommand(String measurement) {

        return new Builder(measurement);
    }

    public static final class Builder {

        String measurement;
        DateTime startTime;
        DateTime endTime;
        /**
         * 存储策略
         */
        String rp;

        Builder(final String measurement) {
            this.measurement = measurement;
        }


        public Builder startTime(DateTime startTime) {
            this.startTime = startTime;
            return this;
        }


        public Builder endTime(DateTime endTime) {
            this.endTime = endTime;
            return this;
        }


        public Builder rp(String rp) {
            this.rp = rp;
            return this;
        }

        /**
         * 相对的业务比较简单，因为是迁移全表，所以此处直接
         * @return
         */
        public String build() {

            Preconditions.checkArgument(!Strings.isNullOrEmpty(measurement), "To be sync measurement must not be null or empty.");
            Preconditions.checkArgument(startTime != null || endTime != null,
                    "StartTime and EndTime must not be null or empty.");
            Preconditions.checkArgument(startTime.isBefore(endTime), "StartTime must be before EndTime.");


            StringBuffer sqlCommand = new StringBuffer(64);
            sqlCommand.append("select * from ").append(Strings.isNullOrEmpty(rp)?(rp+"."):"")
                    .append(this.measurement).append(" where ")
                    .append(" time >= '").append(convertLocalToUTC(startTime))
                    .append("'")
                    .append(" and time <= '").append(convertLocalToUTC(endTime))
                    .append("'");

            return sqlCommand.toString();
        }

    }

}
