package com.sgs.programModel.entity;

public class ProgarmPalyPlan {

    public long startTime;

    public long endTime;

    public String duan;

    public String getDuan() {
        return duan;
    }

    public void setDuan(String duan) {
        this.duan = duan;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    public ProgarmPalyPlan() {
        super();
    }

    @Override
    public String toString() {
        return "ProgarmPalyPlan{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", duan='" + duan + '\'' +
                '}';
    }
}
