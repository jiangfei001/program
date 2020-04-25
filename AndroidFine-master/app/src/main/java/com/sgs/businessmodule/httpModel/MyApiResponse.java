package com.sgs.businessmodule.httpModel;

/**
 * Created by tiansj on 2016/11/30.
 */

public class MyApiResponse {


    public String msg;
    public String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MyApiResponse{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
