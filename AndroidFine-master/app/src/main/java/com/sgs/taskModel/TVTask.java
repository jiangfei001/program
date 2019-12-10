package com.sgs.taskModel;

import com.sgs.commandModel.orderToDb.InstructionRequestManager;
import com.sgs.dbModel.entity.InstructionRequest;
import com.sgs.httpModel.HttpClient;
import com.sgs.httpModel.HttpResponseHandler;
import com.sgs.httpModel.RestApiResponse;
import com.sgs.websocketmodel.InstructionResponse;

import java.util.Date;

import okhttp3.Request;

public abstract class TVTask extends BasicTask {

    public static String TAG = "TVTask";

    private String serverUrl = "http://192.168.0.97:8081/multimedia/api/terminal/callback";

    public InstructionRequest instructionRequest;
    InstructionResponse responseEntity;

    public void setInstructionRequest(InstructionRequest instructionRequest) {
        this.instructionRequest = instructionRequest;
        responseEntity = new InstructionResponse();
        responseEntity.setId(this.instructionRequest.getId());
    }

    @Override
    public void run() {
        if (instructionRequest == null || responseEntity == null) {
            return;
        }
        //更新数据库
        instructionRequest.setStatus(1);
        InstructionRequestManager.getInstance().saveInstructionRequest(instructionRequest);

        responseEntity.setExecuteTime(new Date());
        runTv();
        //告知服务器
        sendEventToService();

        //更新数据库
        instructionRequest.setStatus(2);
        InstructionRequestManager.getInstance().saveInstructionRequest(instructionRequest);
    }

    void sendEventToService() {
        responseEntity.setResult("OK");
        responseEntity.setStatus(1);
        responseEntity.setReceiveTime(new Date());
        HttpClient.postResponseEntity(serverUrl, responseEntity, new HttpResponseHandler() {
            @Override
            public void onSuccess(RestApiResponse response) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }

    public abstract void runTv();
}
