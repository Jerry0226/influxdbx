package com.zsmart.zcm.nms.sync;

import com.zsmart.zcm.nms.influxdb.InfluxDBSelectCommand;
import com.zsmart.zcm.nms.influxdb.InfluxDBUtil;
import com.zsmart.zcm.nms.sync.model.JobExecStats;
import com.zsmart.zcm.nms.sync.model.JobTaskDto;
import com.zsmart.zcm.nms.sync.model.OuterPoint;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


/**
 * 查询的时间为左闭右开的查询条件[5,10)
 */
public class JobTask implements Callable<JobExecStats> {

    public static final String DATETIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 同步成功的数量
     */
    int syncSuccessCount = 0;

    /**
     * 同步失败的数量
     */
    int syncFailCount = 0;

    /**
     * job task的属性
     */
    JobTaskDto jobTaskDto;


    /**
     * 构造方法
     * @param dto
     */
    public JobTask(JobTaskDto dto) {
        this.jobTaskDto = dto;

    }

    @Override
    public JobExecStats call() throws Exception {

        String sqlCommand = InfluxDBSelectCommand.SelectCommand(this.jobTaskDto.getMeasurement())
                .rp(this.jobTaskDto.getSourceRP())
                .startTime(this.jobTaskDto.getStartTime())
                .endTime(this.jobTaskDto.getEndTime()).build();

        Query query = new Query(sqlCommand, this.jobTaskDto.getSourceDatabase());
        QueryResult queryResult = this.jobTaskDto.getSourceInfluxDB().query(query);


        QueryResult.Result  result = InfluxDBUtil.getResult(queryResult);
        if (result != null) {
            QueryResult.Series series = result.getSeries().get(0);
            List<List<Object>> listValues = series.getValues();
            if(!CollectionUtils.isEmpty(listValues)) {
                List<String> columns = series.getColumns();

                for (List<Object> values : listValues) {
                    try {
                        OuterPoint outerPoint = new OuterPoint(columns, values, this.jobTaskDto.getTags());
                        outerPoint.init();
                        Point point = Point.measurement(this.jobTaskDto.getMeasurement()).time(outerPoint.getTime(), TimeUnit.MILLISECONDS).fields(outerPoint.getFields())
                                .tag(outerPoint.getTags()).build();


                        this.jobTaskDto.getDestInfluxDB().write(this.jobTaskDto.getDestDatabase(),
                                this.jobTaskDto.getDestRP(), point);
                        syncSuccessCount ++ ;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        this.syncFailCount ++;
                    }
                }

                this.jobTaskDto.getDestInfluxDB().flush();
            }
        }

        JobExecStats jobExecStats = new JobExecStats();
        jobExecStats.setSyncFailCount(syncFailCount);
        jobExecStats.setSyncSuccessCount(syncSuccessCount);
        System.out.println(this.toString());
        return jobExecStats;
    }




    @Override
    public String toString() {

        return new StringBuffer(128).append("JobTask[ ")
                .append(this.jobTaskDto.getStartTime().toString(DateTimeFormat.forPattern(DATETIME_FORMAT_1)))
                .append(",")
                .append(this.jobTaskDto.getEndTime().toString(DateTimeFormat.forPattern(DATETIME_FORMAT_1)))
                .append("]")
                .append(" syncSuccessCount: ").append(this.syncSuccessCount)
                .append(" syncFailCount: ").append(this.syncFailCount).toString();
    }


}
