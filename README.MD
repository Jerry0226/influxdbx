### influxdb 的在线measurement迁移同步工具，influxdb 原生提供了数据库维度的迁移和同步工具，但针对表的工具缺失

- 同时因为，指标数据量特别大，所以需要提供按时间切割分片任务的能力，提供效率

- 数据进行了不同的采样和存储策略，需要在写入时根据不同时间进行不同的存储策略进行处理，此处也可以根据表名中增加不同的存储策略来进行区分，如迁移表："k2h_raw"."cpu" 与cpu 进行区分



#### 当前支持的命令

```
Built-In Commands
        clear: Clear the shell screen.
        exit, quit: Exit the shell.
        help: Display help about available commands.
        script: Read and execute commands from a file.
        stacktrace: Display the full stacktrace of the last error.

Data Sync Command
        sync: sync influxdb measurement

Influxdb Command
        init: init influxdb config
        reset: reset influxdb config
        show databases: show influxdb databases
        show measurements: show influxdb databases
        use database: Select a database to be operated
```

#### 同步服务所需的接口文件如下

```
{

    "startTime":"2019-11-29 10:02:35",
	"endTime":"2019-11-29 10:03:35",
	"jobTaskCount":10,
	"measurement":"cpu",
	"splitIntervalS":20,
	
	"destClient":{
		"database":"host_metrics",
		"influxDBBatchPeriod":3,
		"influxDBBatchSize":5000,
		"influxDBPassword":"admin",
		"influxDBUrl":"http://xxx.xxx.xxx.xxx:8586/",
		"influxDBUser":"admin",
		"influxdbBatchEnabled":true,
		"rp":"k2h_raw"
	},
	
	"sourceClient":{
		"database":"host_metrics",
		"influxDBBatchPeriod":3,
		"influxDBBatchSize":5000,
		"influxDBPassword":"admin",
		"influxDBUrl":"http://xxx.xxx.xxx.xxx:8586/",
		"influxDBUser":"admin",
		"influxdbBatchEnabled":true,
		"rp":"k2h_raw"
	}
	
	
}
```

| 参数 | 说明 |
| ------ | ------ |
|  startTime| 数据迁移的时间起点 |
| endTime | 数据迁移的时间结束 | 
| jobTaskCount | 迁移切分的任务较多时启动的并发任务数 |
| measurement | 待迁移的表 |
| splitIntervalS | 数据迁移任务切分的时间片，单位为秒 |
| sourceClient | 数据迁移迁移的源数据库 |
| destClient | 数据迁移迁移的目标数据库 |


#### 执行输出

```
JobTask[ 2019-11-29 13:36:06,2019-11-29 13:36:16] syncSuccessCount: 1273 syncFailCount: 0
JobTask[ 2019-11-29 13:35:33,2019-11-29 13:35:43] syncSuccessCount: 1165 syncFailCount: 0
JobTask[ 2019-11-29 13:36:28,2019-11-29 13:36:38] syncSuccessCount: 1189 syncFailCount: 0
JobTask[ 2019-11-29 13:37:23,2019-11-29 13:37:33] syncSuccessCount: 1322 syncFailCount: 0
JobTask[ 2019-11-29 13:36:50,2019-11-29 13:37:00] syncSuccessCount: 1439 syncFailCount: 0
JobTask[ 2019-11-29 13:36:39,2019-11-29 13:36:49] syncSuccessCount: 1261 syncFailCount: 0
JobTask[ 2019-11-29 13:36:17,2019-11-29 13:36:27] syncSuccessCount: 1404 syncFailCount: 0
JobTask[ 2019-11-29 13:38:07,2019-11-29 13:38:17] syncSuccessCount: 1236 syncFailCount: 0
JobTask[ 2019-11-29 13:37:01,2019-11-29 13:37:11] syncSuccessCount: 1213 syncFailCount: 0
JobTask[ 2019-11-29 13:37:45,2019-11-29 13:37:55] syncSuccessCount: 1497 syncFailCount: 0
JobTask[ 2019-11-29 13:37:12,2019-11-29 13:37:22] syncSuccessCount: 1323 syncFailCount: 0
JobTask[ 2019-11-29 13:37:34,2019-11-29 13:37:44] syncSuccessCount: 1231 syncFailCount: 0
JobTask[ 2019-11-29 13:37:56,2019-11-29 13:38:06] syncSuccessCount: 1180 syncFailCount: 0
Sync complete SyncStats {startTime=2019-11-29T13:38:13.385+08:00, endTime=2019-11-29T13:40:48.421+08:00 duration(s): 155, jobCount=655, syncSuccessCount=857196, syncFailCount=0}


```

