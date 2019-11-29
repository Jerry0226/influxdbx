package com.zsmart.zcm.nms.sync.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过query的values 和 column等信息获取point的信息
 */
public class OuterPoint {

    public static final String TIMECOLUMN = "time";

    private String UTCFORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private Map<String, String> tags = new HashMap<>();
    private Long time;

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    private Map<String, Object> fields = new HashMap<>();


    /**
     * 外部传入的变量
     */
    private List<String> columns ;
    private List<Object> values;
    private List<String> tagsName;


    public OuterPoint(List<String> columns, List<Object> values, List<String> tagsName) {
        this.columns = columns;
        this.values = values;
        this.tagsName = tagsName;
    }

    public void init() {
        int count = 0;
        for (String column : columns) {
            if (column.equals(TIMECOLUMN)) {
                DateTime dateTime = DateTime.parse(values.get(count).toString(), DateTimeFormat.forPattern(UTCFORMAT));
                this.time = dateTime.getMillis();
            }
            if (tagsName.contains(column)) {
                tags.put(column, values.get(count).toString());
            }
            else {
                fields.put(column, values.get(count));
            }
            count++;
        }
    }


}