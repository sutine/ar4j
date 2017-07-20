package org.ar4j.elasticsearch;

import org.ar4j.elasticsearch.annotation.TableField;

import java.util.Date;

public class EsBaseEntity extends ElasticSearchActiveRecord/* implements Serializable*/ {

    @TableField("type=integer")
    private Integer dataVersion = 1;

    @TableField("type=date,format=yyyy-MM-dd HH:mm:ss.SSS")
    private Date dataCreateTime = new Date();

    @TableField("type=date,format=yyyy-MM-dd HH:mm:ss.SSS")
    private Date dataUpdateTime = new Date();

    @TableField("type=date,format=yyyy-MM-dd HH:mm:ss.SSS")
    private Date dataDeleteTime;

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public Date getDataCreateTime() {
        return dataCreateTime;
    }

    public void setDataCreateTime(Date dataCreateTime) {
        this.dataCreateTime = dataCreateTime;
    }

    public Date getDataUpdateTime() {
        return dataUpdateTime;
    }

    public void setDataUpdateTime(Date dataUpdateTime) {
        this.dataUpdateTime = dataUpdateTime;
    }

    public Date getDataDeleteTime() {
        return dataDeleteTime;
    }

    public void setDataDeleteTime(Date dataDeleteTime) {
        this.dataDeleteTime = dataDeleteTime;
    }
}
