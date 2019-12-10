package com.sgs.dbModel.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tb_instructionRequest")
public class InstructionRequest {

    /**
     * 指令id
     */
    @DatabaseField
    private int id;

    /**
     * 指令编码
     */
    @DatabaseField
    private int code;

    /**
     * 指令类型
     */
    @DatabaseField
    private String type;

    /**
     * 发送到终端的数据
     */
    @DatabaseField
    private String data;

    /**
     * 过期时间
     */
    @DatabaseField
    private Date expirationTime;


    /**
     * 优先级
     */
    @DatabaseField
    private int priority;

    /**
     * 开始执行时间
     */
    @DatabaseField
    private Date beginTime;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 任务执行状态  0--未执行 1--执行中 2--执行成功 3--执行失败
     */
    @DatabaseField
    private int status = 0;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String toString() {
        return "InstructionRequest{" +
                "id=" + id +
                ", code=" + code +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", expirationTime=" + expirationTime +
                ", priority=" + priority +
                ", beginTime=" + beginTime +
                '}';
    }
}
