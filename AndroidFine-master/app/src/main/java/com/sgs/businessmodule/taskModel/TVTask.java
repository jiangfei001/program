package com.sgs.businessmodule.taskModel;

import com.sgs.AppUrl;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.InstructionRequestManager;
import com.sgs.middle.dbModel.entity.InstructionRequest;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.HttpResponseHandler;
import com.sgs.businessmodule.httpModel.RestApiResponse;
import com.sgs.businessmodule.websocketmodel.InstructionResponse;

import java.util.Date;

import okhttp3.Request;

public abstract class TVTask extends BasicTask {

    public static String TAG = "TVTask";


    public boolean isNeedSend() {
        return isNeedSend;
    }

    public void setNeedSend(boolean needSend) {
        isNeedSend = needSend;
    }

    public boolean isNeedSend = true;

    public InstructionRequest instructionRequest;

    public final InstructionResponse responseEntity = new InstructionResponse();

    public void setInstructionRequest(InstructionRequest instructionRequest) {
        this.instructionRequest = instructionRequest;
        responseEntity.setId(this.instructionRequest.getId());
        responseEntity.setReceiveTime(new Date());
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

    public static long getTimeDifferenceAboutSecond(Date beginTime, Date endTime) {
        // getTime() 方法获取的是毫秒值  将其转为秒返回
        long timeDifference = endTime.getTime() - beginTime.getTime();
        return timeDifference / 1000;
    }

    void sendEventToService() {
        if (isNeedSend()) {
            Date nowDate = new Date();
            responseEntity.setFinishTime(new Date());

            long between = getTimeDifferenceAboutSecond(responseEntity.getReceiveTime(), nowDate);
            responseEntity.setTimes(between);

            responseEntity.setResult("ok");

            HttpClient.postResponseEntity(AppUrl.callbackUrl, responseEntity, new HttpResponseHandler() {
                @Override
                public void onSuccess(RestApiResponse response) {
                }

                @Override
                public void onFailure(Request request, Exception e) {
                }
            });
        }
    }

    void sendEventToServiceZhu() {
            Date nowDate = new Date();
            responseEntity.setFinishTime(new Date());

            long between = getTimeDifferenceAboutSecond(responseEntity.getReceiveTime(), nowDate);
            responseEntity.setTimes(between);

            responseEntity.setResult("ok");

            HttpClient.postResponseEntity(AppUrl.callbackUrl, responseEntity, new HttpResponseHandler() {
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
