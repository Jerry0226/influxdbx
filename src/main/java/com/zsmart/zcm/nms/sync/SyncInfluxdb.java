package com.zsmart.zcm.nms.sync;

import com.google.common.base.Strings;
import com.zsmart.zcm.nms.influxdb.InfluxDBUtil;
import com.zsmart.zcm.nms.sync.model.JobExecStats;
import com.zsmart.zcm.nms.sync.model.JobTaskDto;
import com.zsmart.zcm.nms.sync.model.SyncProperties;
import com.zsmart.zcm.nms.sync.model.SyncStats;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.joda.time.DateTime;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SyncInfluxdb {

    private static final int ASYNC_THRESHOLD = 100;

    private static final String SHOWTAGS_PREFIX = "SHOW TAG KEYS FROM ";

    public static final String DQUOTES = "\"";

    public SyncStats sync(SyncProperties syncProperties) {

        SyncStats syncStats = new SyncStats();

        syncStats.setStartTime(DateTime.now());

        syncProperties.checkPreCondition();
        List<JobTask> jobTaskList = new ArrayList<>();


        InfluxDB sourceInfluxdb = InfluxDBUtil.getInfluxDB(syncProperties.getSourceClient());

        List<String> tags = getTags(syncProperties.getMeasurement()
                , syncProperties.getSourceClient().getRp(), syncProperties.getSourceClient().getDatabase(), sourceInfluxdb);

        InfluxDB destInfluxdb = InfluxDBUtil.getInfluxDB(syncProperties.getDestClient());

        int jobSize = (int) (syncProperties.getEndTime().getMillis() - syncProperties.getStartTime().getMillis()) / (syncProperties.getSplitIntervalS() * 1000);

        ThreadPoolExecutor executor = null;

        if (jobSize > ASYNC_THRESHOLD) {
            executor = new ThreadPoolExecutor(syncProperties.getJobTaskCount(), syncProperties.getJobTaskCount(), 1,
                    TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(jobSize));
        }
        else {
            executor = new ThreadPoolExecutor(1, 1, 1,
                    TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(jobSize));
        }


        DateTime startTime = syncProperties.getStartTime();

        DateTime endTime = syncProperties.getEndTime();

        List<Future> taskList = new ArrayList<>();

        while (startTime.isBefore(endTime)) {

            JobTaskDto jobTaskDto = new JobTaskDto(syncProperties, sourceInfluxdb, destInfluxdb);

            DateTime jobStartTime = new DateTime(startTime.getMillis());

            startTime = startTime.plusSeconds(syncProperties.getSplitIntervalS());

            DateTime jobEndTime = new DateTime(startTime.getMillis());

            jobTaskDto.setStartTime(jobStartTime);
            jobTaskDto.setEndTime(jobEndTime);

            jobTaskDto.setTags(tags);

            /**
             * 防止同步重复
             */
            startTime = startTime.plusSeconds(1);

            JobTask jobTask = new JobTask(jobTaskDto);

            Future<JobExecStats> future = executor.submit(jobTask);

            taskList.add(future);
        }


        syncStats.setJobCount(taskList.size());

        for (Future future : taskList) {
            try {
                JobExecStats jobExecStats = (JobExecStats) future.get();
                syncStats.addSyncFailCount(jobExecStats.getSyncFailCount());
                syncStats.addSyncSuccessCount(jobExecStats.getSyncSuccessCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        sourceInfluxdb.flush();
        sourceInfluxdb.close();
        destInfluxdb.flush();
        destInfluxdb.close();

        executor.shutdown();


        syncStats.setEndTime(DateTime.now());

        System.out.println("Sync complete " + syncStats.toString());

        return syncStats;

    }


    /**
     * 获取待同步表的tags集合信息
     * @param measurement
     * @param rp
     * @param database
     * @param influxDB
     * @return
     */
    private List<String> getTags(String measurement, String rp, String database, InfluxDB influxDB) {

        String sql = getShowtagsSql(measurement, rp);
        Query query = new Query(sql, database);
        QueryResult queryResult = influxDB.query(query);

        List<String> tags = new ArrayList<>();

        QueryResult.Result  result = InfluxDBUtil.getResult(queryResult);
        if (result != null) {
            QueryResult.Series series = result.getSeries().get(0);
            List<List<Object>> listValues = series.getValues();
            if (!CollectionUtils.isEmpty(listValues)) {
                for (List<Object> values : listValues) {
                    for (Object tagName : values) {
                        tags.add(tagName.toString());
                    }
                }

            }
        }
        return tags;
    }

    private String getShowtagsSql(String measurement, String rp) {

        String measurementTemp = Strings.isNullOrEmpty(rp) ? measurement : DQUOTES + rp + DQUOTES + "." + DQUOTES + measurement + DQUOTES;
        return SHOWTAGS_PREFIX + measurementTemp;
    }


}
