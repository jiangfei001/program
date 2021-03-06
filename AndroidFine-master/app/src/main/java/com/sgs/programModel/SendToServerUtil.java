package com.sgs.programModel;

import com.uiModel.loginUtil.LoginUtil;
import com.zhangke.zlog.ZLog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.AppContext;
import com.sgs.AppUrl;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.MyApiResponse;
import com.sgs.businessmodule.httpModel.MyHttpResponseHandler;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg;
import com.sgs.businessmodule.upReportModel.RepHotReport;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.businessmodule.websocketmodel.InstructionResponse;
import com.sgs.middle.utils.DateUtil;
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
        ZLog.e(TAG, "上报节目处理结果");
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
                ZLog.e(TAG, "sendEventToService" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }

    //节目列表全量  type 0 增加 1 删除
    public static void sendAddOrDelProList(ArrayList<ProgarmPalyInstructionVo> progarmPalyInstructionVos, int type) {
        if (progarmPalyInstructionVos != null) {
            ZLog.e(TAG, "节目列表全量增加" + progarmPalyInstructionVos.size() + "type" + type);
        } else {
            ZLog.e(TAG, "节目列表全量增加null" + "type" + type);
        }
        ArrayList<ProListVo> responseEntity = new ArrayList<>();
        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(progarmPalyInstructionVos.get(i).getId());
            proListVo.setTerminalIdentity(LoginUtil.getTerminalIdentity());
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
                ZLog.e(TAG, "sendEventToAllProList onSuccess" + response.msg);
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
            ZLog.e(TAG, "节目列表全量增加" + progarmPalyInstructionVos.size() + "type" + type);
        } else {
            ZLog.e(TAG, "节目列表全量增加null" + "type" + type);
        }

        ArrayList<ProListVo> terminalProgramEntity = new ArrayList<>();
        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(progarmPalyInstructionVos.get(i).getId());
            proListVo.setTerminalIdentity(LoginUtil.getTerminalIdentity());
            StringBuilder sb = new StringBuilder();
            PublicationPlanVo publicationPlanVo = JSON.parseObject(progarmPalyInstructionVos.get(i).getPublicationPlan(), new TypeReference<PublicationPlanVo>() {
            });
            sb.append(publicationPlanVo.getDeadline() + "~" + publicationPlanVo.getDeadlineV());
            proListVo.setTimeQuantum(sb.toString());
            proListVo.setType(type);
            proListVo.setPublicationTime(DateUtil.getDateStr(progarmPalyInstructionVos.get(i).getReceiveTime()));
            proListVo.setLimitTime(publicationPlanVo.getDeadline());
            terminalProgramEntity.add(proListVo);
        }

        ArrayList<ProListVo> daylist = new ArrayList<>();
        for (int i = 0; i < todays.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(todays.get(i).getId());
            proListVo.setTerminalIdentity(LoginUtil.getTerminalIdentity());
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
        hashMap.put("terminalIdentity", LoginUtil.getTerminalIdentity());

        HttpClient.postHashMapEntity(AppUrl.addTerminalProgramListUrl, hashMap, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                ZLog.e(TAG, "sendEventToAllProList onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }


    //当天节目全量接口
    public static void sendEventToToDayAll(LinkedList<ProgarmPalyInstructionVo> progarmPalyInstructionVos) {
        if (progarmPalyInstructionVos != null) {
            ZLog.e(TAG, "当天节目全量接口" + progarmPalyInstructionVos.size());
        } else {
            ZLog.e(TAG, "当天节目全量接口null");
        }
        ArrayList<ProListVo> responseEntity = new ArrayList<>();

        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(progarmPalyInstructionVos.get(i).getId());
            proListVo.setTerminalIdentity(LoginUtil.getTerminalIdentity());
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
                ZLog.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }

    //当天节目全量接口
    public static void sendNowPro(InstructionResponse instructionResponse, ArrayList<ProgarmPalyInstructionVo> progarmPalyInstructionVos) {
        if (progarmPalyInstructionVos != null) {
            ZLog.e(TAG, "当天节目全量接口" + progarmPalyInstructionVos.size());
        } else {
            ZLog.e(TAG, "当天节目全量接口null");
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
                ZLog.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
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
    public static void sendScenctToServer(List<ScenceReport> scenceReports, MyYewuResponseHandle myYewuResponseHandle) {

        if (scenceReports != null) {
            ZLog.e(TAG, "sendScenctToServer:" + scenceReports.size());
        } else {
            ZLog.e(TAG, "sendScenctToServer:null");
        }
        HashMap hashMap = new HashMap();
        hashMap.put("repPalyProgramEntitys", com.alibaba.fastjson.JSON.toJSONString(scenceReports));
        HttpClient.postHashMapEntity(AppUrl.addRepPalyProgramList, hashMap, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                ZLog.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                ZLog.e(TAG, "sendEventToToDayAll onSuccess" + e.getMessage());
            }
        });
    }

    //删除{'id':11,'msgStatus':3}
    public static void sendMsgDelToServer(MuTerminalMsg muTerminalMsg) {

        //finishTime
        muTerminalMsg.setFinishTime(muTerminalMsg.getHasplay() + "");
        //beginTime
        //endDate
        muTerminalMsg.setMsgStatus("0");
        muTerminalMsg.setEndDate(DateUtil.getNowDate());

        // hashMap.put("paramMap", com.alibaba.fastjson.JSON.toJSONString(muTerminalMsg));

        HttpClient.postObjectEntity(AppUrl.changeMsgStatus, muTerminalMsg, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                ZLog.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }


    //当天节目全量接口
    public static void sendRepHotareaToServer(List<RepHotReport> repHotReports, MyYewuResponseHandle myYewuResponseHandle) {

        if (repHotReports != null) {
            ZLog.e(TAG, "sendScenctToServer:" + repHotReports.size());
        } else {
            ZLog.e(TAG, "sendScenctToServer:null");
        }

        HashMap hashMap = new HashMap();
        hashMap.put("repHotareaClickEntitys", com.alibaba.fastjson.JSON.toJSONString(repHotReports));

        HttpClient.postHashMapEntity(AppUrl.addRepHotareaClickList, hashMap, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                ZLog.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }

    public static void UpdateApkJsonToServer(List<RepHotReport> repHotReports, MyYewuResponseHandle myYewuResponseHandle) {

        if (repHotReports != null) {
            ZLog.e(TAG, "sendScenctToServer:" + repHotReports.size());
        } else {
            ZLog.e(TAG, "sendScenctToServer:null");
        }

        HashMap hashMap = new HashMap();
        hashMap.put("repHotareaClickEntitys", com.alibaba.fastjson.JSON.toJSONString(repHotReports));

        HttpClient.postHashMapEntity(AppUrl.addRepHotareaClickList, hashMap, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                ZLog.e(TAG, "sendEventToToDayAll onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }


    public static void UpdateSysMaterialDataToServer(HashMap hashMap) {
        /*
        类型：0:全量   1：新增  2：删除
        {
            "uuidList":["32e54e72d6b54af5b9c8a60c85014a6e","32e54e72d6b54af5b9c8a60c85014a61"],
            "terminalIdentity":"078a0551-b333-323c-a09d-4af272baa82a",
                "type":0
        }*/

        HttpClient.postHashMapEntity(AppUrl.sysMaterialData, hashMap, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(MyApiResponse response) {
                ZLog.e(TAG, "sysMaterialData onSuccess" + response.msg);
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }
        });
    }


    public interface MyYewuResponseHandle {
        void onSuccess(MyApiResponse response);

        void onFailure(Request request, Exception e);
    }

}
