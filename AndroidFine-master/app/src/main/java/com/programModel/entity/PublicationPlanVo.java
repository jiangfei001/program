package com.programModel.entity;

import java.util.List;

/**
 * 节目发布计划对象
 *
 * @author qcxian
 */
public class PublicationPlanVo {


    private String deadlineV;

    private String resolution;

    /**
     * 截止时间
     */
    private String deadline;

    /**
     * 独占节目
     */
    private boolean exclusive;
    /**
     * 计划类型    0： 循环播放  1：按周播放  2：自定义播放
     */
    private Integer planType = 0;

    /**
     * 周播放日程
     */
    private List<ProgarmPalySchedule> weekPalySchedule;

    /**
     * 自定义播放日程
     */
    private List<ProgarmPalySchedule> customizedPalySchedule;


    public String getDeadline() {
        return deadline;
    }

    /**
     * 为了页面的回显正常
     *
     * @return
     */
    public String getDeadlineV() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public Integer getPlanType() {
        return planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    public List<ProgarmPalySchedule> getWeekPalySchedule() {
        return weekPalySchedule;
    }

    public void setWeekPalySchedule(List<ProgarmPalySchedule> weekPalySchedule) {
        this.weekPalySchedule = weekPalySchedule;
    }

    public List<ProgarmPalySchedule> getCustomizedPalySchedule() {
        return customizedPalySchedule;
    }

    public void setCustomizedPalySchedule(List<ProgarmPalySchedule> customizedPalySchedule) {
        this.customizedPalySchedule = customizedPalySchedule;
    }

    public void setDeadlineV(String deadlineV) {
        this.deadlineV = deadlineV;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
