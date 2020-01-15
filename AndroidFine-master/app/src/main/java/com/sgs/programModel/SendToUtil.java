package com.sgs.programModel;

import com.sgs.AppUrl;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.HttpResponseHandler;
import com.sgs.businessmodule.httpModel.RestApiResponse;
import com.sgs.businessmodule.websocketmodel.InstructionResponse;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

import java.util.Date;

import okhttp3.Request;

public class SendToUtil {

    public static void sendEventToService(ProgarmPalyInstructionVo progarmPalyInstructionVo) {

        InstructionResponse responseEntity = new InstructionResponse();
        responseEntity.setId(progarmPalyInstructionVo.getZlid());
        responseEntity.setReceiveTime(progarmPalyInstructionVo.getReceiveTime());
        responseEntity.setExecuteTime(progarmPalyInstructionVo.getExecuteTime());
        Date nowDate = new Date();
        responseEntity.setFinishTime(nowDate);
        long between = getTimeDifferenceAboutSecond(progarmPalyInstructionVo.getReceiveTime(), nowDate);
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

    public static long getTimeDifferenceAboutSecond(Date beginTime, Date endTime) {
        // getTime() 方法获取的是毫秒值  将其转为秒返回
        long timeDifference = endTime.getTime() - beginTime.getTime();
        return timeDifference / 1000;
    }
}
