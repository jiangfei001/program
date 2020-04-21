package com.sgs.programModel;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.AppContext;
import com.sgs.AppUrl;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.MyApiResponse;
import com.sgs.businessmodule.httpModel.MyHttpResponseHandler;
import com.sgs.businessmodule.upReportModel.RepHotReport;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.businessmodule.websocketmodel.InstructionResponse;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.programModel.entity.ProListVo;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgarmPalyPlan;
import com.sgs.programModel.entity.PublicationPlanVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
        responseEntity.setInstructionType("110");
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

    //节目列表全量  type 0 增加 1 删除
    public static void sendAddOrDelProList(ArrayList<ProgarmPalyInstructionVo> progarmPalyInstructionVos, int type) {
        if (progarmPalyInstructionVos != null) {
            Log.e(TAG, "节目列表全量增加" + progarmPalyInstructionVos.size() + "type" + type);
        } else {
            Log.e(TAG, "节目列表全量增加null" + "type" + type);
        }
        ArrayList<ProListVo> responseEntity = new ArrayList<>();
        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(progarmPalyInstructionVos.get(i).getId());
            proListVo.setTerminalIdentity(DeviceUtil.getUniqueID(AppContext.getInstance()));
            StringBuilder sb = new StringBuilder();
            PublicationPlanVo publicationPlanVo = JSON.parseObject(progarmPalyInstructionVos.get(i).getPublicationPlan(), new TypeReference<PublicationPlanVo>() {
            });
            sb.append(publicationPlanVo.getDeadline() + "~" + publicationPlanVo.getDeadlineV());
            proListVo.setTimeQuantum(sb.toString());
            proListVo.setType(type);
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


    /*第一个参数：terminalProgramEntity 第一个就是节目列表的数据
    第二个参数就是类型  0：全量更新  1：增量新增  2：删除    3： 无更新
    第三个参数就是 daylist 当天节目*/

    public static void sendAddOrDelProListNew(ArrayList<ProgarmPalyInstructionVo> progarmPalyInstructionVos, int type, LinkedList<ProgarmPalyInstructionVo> todays) {

        if (progarmPalyInstructionVos != null) {
            Log.e(TAG, "节目列表全量增加" + progarmPalyInstructionVos.size() + "type" + type);
        } else {
            Log.e(TAG, "节目列表全量增加null" + "type" + type);
        }

        ArrayList<ProListVo> terminalProgramEntity = new ArrayList<>();
        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(progarmPalyInstructionVos.get(i).getId());
            proListVo.setTerminalIdentity(DeviceUtil.getUniqueID(AppContext.getInstance()));
            StringBuilder sb = new StringBuilder();
            PublicationPlanVo publicationPlanVo = JSON.parseObject(progarmPalyInstructionVos.get(i).getPublicationPlan(), new TypeReference<PublicationPlanVo>() {
            });
            sb.append(publicationPlanVo.getDeadline() + "~" + publicationPlanVo.getDeadlineV());
            proListVo.setTimeQuantum(sb.toString());
            proListVo.setType(type);
            terminalProgramEntity.add(proListVo);
        }

        ArrayList<ProListVo> daylist = new ArrayList<>();
        for (int i = 0; i < todays.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(todays.get(i).getId());
            proListVo.setTerminalIdentity(DeviceUtil.getUniqueID(AppContext.getInstance()));
            StringBuilder sb = new StringBuilder();
            List<ProgarmPalyPlan> okProgarms = todays.get(i).getPublicationPlanObject().getOkProgarms();
            for (int t = 0; t < okProgarms.size(); t++) {
                if (t > 0) {
                    sb.append("|");
                }
                ProgarmPalyPlan progarmPalyPlan = okProgarms.get(t);
                sb.append(progarmPalyPlan.getDuan());
            }
            proListVo.setTimeQuantum(sb.toString());
            proListVo.setType(0);
            daylist.add(proListVo);
        }

        HashMap hashMap = new HashMap();
        hashMap.put("terminalProgramEntity", com.alibaba.fastjson.JSON.toJSONString(terminalProgramEntity));
        hashMap.put("type", type);
        hashMap.put("daylist", com.alibaba.fastjson.JSON.toJSONString(daylist));
        hashMap.put("terminalIdentity", DeviceUtil.getUniqueID(AppContext.getInstance()));

        HttpClient.postHashMapEntity(AppUrl.addTerminalProgramListUrl, hashMap, new MyHttpResponseHandler() {
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
    public static void sendEventToToDayAll(LinkedList<ProgarmPalyInstructionVo> progarmPalyInstructionVos) {
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

    //当天节目全量接口
    public static void sendNowPro(InstructionResponse instructionResponse, ArrayList<ProgarmPalyInstructionVo> progarmPalyInstructionVos) {
        if (progarmPalyInstructionVos != null) {
            Log.e(TAG, "当天节目全量接口" + progarmPalyInstructionVos.size());
        } else {
            Log.e(TAG, "当天节目全量接口null");
        }
        ArrayList<Integer> responseEntity = new ArrayList<>();

        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            responseEntity.add(progarmPalyInstructionVos.get(i).getId());
        }

        HashMap hashMap = new HashMap();
        hashMap.put("nowproids", responseEntity);

        Date nowDate = new Date();
        instructionResponse.setFinishTime(new Date());

        long between = getTimeDifferenceAboutSecond(instructionResponse.getReceiveTime(), nowDate);
        instructionResponse.setTimes(between);
        instructionResponse.setResult(com.alibaba.fastjson.JSON.toJSONString(hashMap));

        HttpClient.postResponseEntity(AppUrl.addDayProgramList, instructionResponse, new MyHttpResponseHandler() {
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


    //当天节目全量接口
    public static void sendScenctToServer(List<ScenceReport> scenceReports) {

        if (scenceReports != null) {
            Log.e(TAG, "sendScenctToServer:" + scenceReports.size());
        } else {
            Log.e(TAG, "sendScenctToServer:null");
        }
        HashMap hashMap = new HashMap();
        hashMap.put("repPalyProgramEntitys", scenceReports);

        HttpClient.postHashMapEntity(AppUrl.addRepPalyProgramList, hashMap, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                Log.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }

    //当天节目全量接口
    public static void sendRepHotareaToServer(List<RepHotReport> repHotReports) {

        if (repHotReports != null) {
            Log.e(TAG, "sendScenctToServer:" + repHotReports.size());
        } else {
            Log.e(TAG, "sendScenctToServer:null");
        }

        HashMap hashMap = new HashMap();
        hashMap.put("repPalyProgramEntitys", repHotReports);

        HttpClient.postHashMapEntity(AppUrl.addRepHotareaClickList, hashMap, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                Log.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }

}
