package com.sgs.programModel;

import android.util.Log;

import com.sgs.AppContext;
import com.sgs.AppUrl;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.HttpResponseHandler;
import com.sgs.businessmodule.httpModel.MyApiResponse;
import com.sgs.businessmodule.httpModel.MyHttpResponseHandler;
import com.sgs.businessmodule.httpModel.RestApiResponse;
import com.sgs.businessmodule.websocketmodel.InstructionResponse;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.programModel.entity.ProListVo;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgarmPalyPlan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

public class SendToServerUtil {

    public static String TAG = "SendToServerUtil";

    public static void sendEventToService(ProgarmPalyInstructionVo progarmPalyInstructionVo) {
        Log.e(TAG, "上报节目处理结果");
        InstructionResponse responseEntity = new InstructionResponse();
        responseEntity.setId(progarmPalyInstructionVo.getZlid());
        responseEntity.setReceiveTime(progarmPalyInstructionVo.getReceiveTime());
        responseEntity.setExecuteTime(progarmPalyInstructionVo.getExecuteTime());
        Date nowDate = new Date();
        responseEntity.setFinishTime(nowDate);
        long between = getTimeDifferenceAboutSecond(progarmPalyInstructionVo.getReceiveTime(), nowDate);
        responseEntity.setTimes(between);
        responseEntity.setResult("ok");

        HttpClient.postResponseEntity(AppUrl.callbackUrl, responseEntity, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                Log.e(TAG, "sendEventToService" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }

    //节目列表增量
    public static void sendEventToAddProList(ProgarmPalyInstructionVo progarmPalyInstructionVo) {
        if (progarmPalyInstructionVo != null) {
            Log.e(TAG, "节目列表全量" + progarmPalyInstructionVo.toString());
        } else {
            Log.e(TAG, "节目列表增量null");
        }

        ArrayList<ProListVo> responseEntity = new ArrayList<>();
        ProListVo proListVo = new ProListVo();
        if (progarmPalyInstructionVo != null) {
            proListVo.setProgramId(progarmPalyInstructionVo.getId());
            proListVo.setTerminalIdentity(DeviceUtil.getUniqueID(AppContext.getInstance()));
            StringBuilder sb = new StringBuilder();
            List<ProgarmPalyPlan> okProgarms = progarmPalyInstructionVo.getPublicationPlanObject().getOkProgarms();
            for (int t = 0; t < okProgarms.size(); t++) {
                if (t > 0) {
                    sb.append("|");
                }
                ProgarmPalyPlan progarmPalyPlan = okProgarms.get(t);
                sb.append(progarmPalyPlan.getDuan());
            }
            proListVo.setTimeQuantum(sb.toString());
            proListVo.setType(0);
        }
        responseEntity.add(proListVo);

        HttpClient.postResponseList(AppUrl.addTerminalProgramListUrl, responseEntity, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                Log.e(TAG, "sendEventToAddProList" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }

    //节目列表全量
    public static void sendEventToAllProList(ArrayList<ProgarmPalyInstructionVo> progarmPalyInstructionVos) {
        if (progarmPalyInstructionVos != null) {
            Log.e(TAG, "节目列表全量增加" + progarmPalyInstructionVos.size());
        } else {
            Log.e(TAG, "节目列表全量增加null");
        }
        ArrayList<ProListVo> responseEntity = new ArrayList<>();
        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(progarmPalyInstructionVos.get(i).getId());
            proListVo.setTerminalIdentity(DeviceUtil.getUniqueID(AppContext.getInstance()));
            StringBuilder sb = new StringBuilder();
            sb.append(progarmPalyInstructionVos.get(i).getPublicationPlanObject().getDeadline() + "~" + progarmPalyInstructionVos.get(i).getPublicationPlanObject().getDeadlineV());
            proListVo.setTimeQuantum(sb.toString());
            proListVo.setType(0);
            responseEntity.add(proListVo);
        }

        HttpClient.postResponseList(AppUrl.addTerminalProgramListUrl, responseEntity, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                Log.e(TAG, "sendEventToAllProList onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });

    }

    //当天节目全量接口
    public static void sendEventToToDayAll(ArrayList<ProgarmPalyInstructionVo> progarmPalyInstructionVos) {
        if (progarmPalyInstructionVos != null) {
            Log.e(TAG, "当天节目全量接口" + progarmPalyInstructionVos.size());
        } else {
            Log.e(TAG, "当天节目全量接口null");
        }
        ArrayList<ProListVo> responseEntity = new ArrayList<>();

        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(progarmPalyInstructionVos.get(i).getId());
            proListVo.setTerminalIdentity(DeviceUtil.getUniqueID(AppContext.getInstance()));
            StringBuilder sb = new StringBuilder();
            List<ProgarmPalyPlan> okProgarms = progarmPalyInstructionVos.get(i).getPublicationPlanObject().getOkProgarms();
            for (int t = 0; t < okProgarms.size(); t++) {
                if (t > 0) {
                    sb.append("|");
                }
                ProgarmPalyPlan progarmPalyPlan = okProgarms.get(t);
                sb.append(progarmPalyPlan.getDuan());
            }
            proListVo.setTimeQuantum(sb.toString());
            proListVo.setType(0);
            responseEntity.add(proListVo);
        }

        HttpClient.postResponseList(AppUrl.addDayProgramList, responseEntity, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                Log.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
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
