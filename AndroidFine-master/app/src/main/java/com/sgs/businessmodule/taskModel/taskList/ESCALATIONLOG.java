package com.sgs.businessmodule.taskModel.taskList;

import com.alibaba.fastjson.JSON;
import com.qiniu.android.http.ResponseInfo;
import com.sgs.AppContext;
import com.sgs.AppUrl;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.MyApiResponse;
import com.sgs.businessmodule.httpModel.MyHttpResponseHandler;
import com.sgs.businessmodule.qiniuModel.QiniuUpHelper;
import com.sgs.businessmodule.taskModel.TVTask;
import com.zhangke.zlog.ZLog;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import okhttp3.Request;

public class ESCALATIONLOG extends TVTask {

    @Override
    public void runTv() {

        isNeedSend = false;

        String data = this.instructionRequest.getData();

        com.alibaba.fastjson.JSONObject maps2 = (com.alibaba.fastjson.JSONObject) JSON.parse(data);

        String uptoken = "";

        String path = "";

        if (maps2.get("token") != null) {
            uptoken = (String) maps2.get("token");
        }
        if (maps2.get("path") != null) {
            path = (String) maps2.get("path");

        }
        final String finalPath = path;
        QiniuUpHelper.uploadLog(AppContext.getInstance().getNowActivity(), false, uptoken, new ESCALATIONLOG.BackUrl() {
            @Override
            public String getUrlandName(String key, ResponseInfo info, JSONObject response) {
                responseEntity.setId(ESCALATIONLOG.this.instructionRequest.getId());
                Date nowDate = new Date();
                responseEntity.setFinishTime(nowDate);
                long between = getTimeDifferenceAboutSecond(responseEntity.getReceiveTime(), nowDate);
                responseEntity.setTimes(between);
                HashMap hashMap = new HashMap();
                hashMap.put("path", finalPath + "/" + key);
                responseEntity.setResult(JSON.toJSONString(hashMap));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient.postResponseEntity(AppUrl.callbackUrl, responseEntity, new MyHttpResponseHandler() {
                            @Override
                            public void onSuccess(MyApiResponse response) {
                                ZLog.e(TAG, "TAKESCREEN" + response.msg);
                            }

                            @Override
                            public void onFailure(Request request, Exception e) {
                                ZLog.e("onFailure", "onFailure");
                            }
                        });
                    }
                }).start();
                return null;
            }
        });
    }

    public interface BackUrl {
        String getUrlandName(String key, ResponseInfo info, JSONObject response);
    }

    public static void main(String[] args) {
        try {
            com.alibaba.fastjson.JSONObject maps2 = (com.alibaba.fastjson.JSONObject) JSON.parse("{\"token\":\"UxVCg4Xl0tmDmGPK2L_V9t3qH70gmONr1IbzdxuR:meOdwFdjrBiHebF6VwMJ9cK_snQ=:eyJzY29wZSI6Im11bHRpbWVkaWEtdGVzdCIsImRlYWRsaW5lIjoxNTc4MjEzMzE1fQ==\"}");

            if (maps2.get("token") != null) {
                String s = (String) maps2.get("token");
                System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
