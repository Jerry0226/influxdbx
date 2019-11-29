package com.zsmart.zcm.nms.command;


import com.alibaba.fastjson.JSON;
import com.zsmart.zcm.nms.sync.SyncInfluxdb;
import com.zsmart.zcm.nms.sync.model.SyncProperties;
import com.zsmart.zcm.nms.sync.model.SyncStats;
import org.apache.commons.io.FileUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;

@ShellComponent
public class DataSyncCommand {
    @ShellMethod(value = "sync influxdb measurement", key = "sync")
    public String syncInfluxdb(@ShellOption("--file") String syncfile ) {
        SyncStats syncStats = new SyncStats();
        try {
            String content = FileUtils.readFileToString(new File(syncfile));
            SyncProperties syncProperties = JSON.parseObject(content, SyncProperties.class);
            SyncInfluxdb syncInfluxdb = new SyncInfluxdb();
            syncStats = syncInfluxdb.sync(syncProperties);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return syncStats.toString();
    }

}
