package com.sgs.websocketmodel;

import java.util.Date;

public class InstructionResponse {

    /**
     * 指令id
     */
    private int id;

    /**
     * 终端返回的结果
     */
    private String result;

    /**
     * 接收时间
     */
    private Date receiveTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 执行状态
     */
    private int status;

    /**
     * 开始执行时间
     */
    private Date executeTime;

    /**
     * 执行时长
     */
    private long times;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
