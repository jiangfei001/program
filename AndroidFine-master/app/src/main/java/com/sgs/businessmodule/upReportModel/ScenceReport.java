package com.sgs.businessmodule.upReportModel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tb_scenceReport")
public class ScenceReport {
    /**
     * 指令Scence
     */
    @DatabaseField(generatedId = true)
    private int id;
    //日期、场景 维度 来 记录  每天的场景播放次数和时间
    @DatabaseField
    private String dateStr;
    @DatabaseField
    private int number;
    @DatabaseField
    private long time;
    @DatabaseField
    private int scenceid;
    @DatabaseField
    private int count;

    public int getScenceid() {
        return scenceid;
    }

    public void setScenceid(int scenceid) {
        this.scenceid = scenceid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ScenceReport{" +
                "id=" + id +
                ", datdatestre=" + dateStr +
                ", number=" + number +
                ", time=" + time +
                ", scenceid=" + scenceid +
                '}';
    }
}
