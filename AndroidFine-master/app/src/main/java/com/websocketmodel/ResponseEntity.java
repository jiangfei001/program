package com.websocketmodel;

/**
 * 后台接口返回的数据格式
 * Created by ZhangKe on 2018/6/27.
 */
public class ResponseEntity {
    /**
     * type
     */
    private int code;
    /**
     * type
     */
    private String type;
    /**
     * 命令ID
     */
    private int id;
    /**
     *
     */
    private String object;
    /**
     *
     */
    private String endtime;
    /**
     *
     */
    private int priority = 0;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}
