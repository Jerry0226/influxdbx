package com.zsmart.zcm.nms.command;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zsmart.zcm.nms.influxdb.InfluxDBClientConfig;
import com.zsmart.zcm.nms.influxdb.InfluxDBUtil;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class InfluxdbCommand {


    private InfluxDB inlfuxDB = null;

    private String database = "_internal";


    @ShellMethod(value = "init influxdb config", key = "init")
    public String init(@ShellOption("--url") String influxDBUrl, @ShellOption(value = "--user", defaultValue = "")String influxDBUser,
                               @ShellOption(value = "--password", defaultValue = "")String influxDBPassword) {

        inlfuxDB = InfluxDBUtil.getInfluxDB(InfluxDBClientConfig.influxDBUrl(influxDBUrl, influxDBUser, influxDBPassword).build());

        return inlfuxDB.toString();
    }

    @ShellMethod(value = "show influxdb databases", key = "show databases")
    public String showdatabase() {

        if (inlfuxDB == null) {
            return  "Please initialize first";
        }

        QueryResult result = inlfuxDB.query(new Query("show databases", "_internal"));
        String resultJson = JSON.toJSONString(result, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        return resultJson;
    }


    @ShellMethod(value = "Select a database to be operated", key = "use database")
    public String usedatabase(String database) {

        if (inlfuxDB == null) {
            return  "Please initialize first";
        }

        this.database = database;
        return "use database success";
    }

    @ShellMethod(value = "show influxdb databases", key = "show measurements")
    public String showmeasurements() {

        if (inlfuxDB == null) {
            return  "Please initialize first";
        }

        QueryResult result = inlfuxDB.query(new Query("show MEASUREMENTS", this.database));
        String resultJson = JSON.toJSONString(result, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        return resultJson;
    }


    @ShellMethod(value = "reset influxdb config", key = "reset")
    public String reset() {

        if (inlfuxDB != null) {
            this.database = "_internal";
            inlfuxDB.close();
        }
        return "reset success";
    }


}

