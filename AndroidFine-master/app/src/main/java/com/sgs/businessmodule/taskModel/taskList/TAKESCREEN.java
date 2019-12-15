package com.sgs.businessmodule.taskModel.taskList;

import com.qiniu.android.http.ResponseInfo;
import com.sgs.AppContext;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.HttpResponseHandler;
import com.sgs.businessmodule.httpModel.RestApiResponse;
import com.sgs.businessmodule.qiniuModel.QiniuUpHelper;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.websocketmodel.InstructionResponse;

import org.json.JSONObject;

import java.util.Date;

import okhttp3.Request;

public class TAKESCREEN extends TVTask {

    String url="http://192.168.0.97:8081/multimedia/api/terminal/callback";

    @Override
    public void runTv() {
        QiniuUpHelper.upload(AppContext.getInstance().getNowActivity(), false, new BackUrl() {
            @Override
            public String getUrlandName(String key, ResponseInfo info, JSONObject response) {

                final InstructionResponse responseEntity = new InstructionResponse();
                responseEntity.setId(123);
                responseEntity.setExecuteTime(new Date());
                responseEntity.setResult("123123");
                responseEntity.setStatus(1);
                responseEntity.setReceiveTime(new Date());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient.postResponseEntity("http://192.168.0.97:8081/multimedia/api/terminal/callback", responseEntity, new HttpResponseHandler() {
                            @Override
                            public void onSuccess(RestApiResponse response) {

                            }

                            @Override
                            public void onFailure(Request request, Exception e) {

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


}
