### influxdb 的在线迁移同步工具，influxdb 原生提供了数据库维度的迁移和同步工具，但针对表的工具缺失

#### 需求：nms-influxdb 集群提供了标准的influxdb 集群能力，在nms-influxdb做分片路由变更时，没有数据迁移的能力会导致数据丢失，需要提供一种用户可选的迁移工具

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


