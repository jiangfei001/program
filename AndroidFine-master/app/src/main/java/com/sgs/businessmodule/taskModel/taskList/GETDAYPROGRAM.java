package com.sgs.businessmodule.taskModel.taskList;


import android.util.Log;

import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.entity.ProListVo;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgarmPalyPlan;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GETDAYPROGRAM extends TVTask {

    LinkedList<ProgarmPalyInstructionVo> todaylist = new LinkedList<>();

    @Override
    public void runTv() {
        Log.e(TAG, "GETNEWPROGRAMMESSAGE:");
        todaylist = ProgramScheduledManager.getInstance().getProlistToday();
        // SendToServerUtil.sendNowPro(this.responseEntity, progarmPalyInstructionVos);
    }

    @Override
    public void setResult() {
        /*if (todaylist != null) {
            Log.e(TAG, "当天节目全量接口" + todaylist.size());

            StringBuilder nowproid = new StringBuilder();

            for (int i = 0; i < todaylist.size(); i++) {
                if (i != 0) {
                    nowproid.append(",");
                }
                nowproid.append(todaylist.get(i).getId());
            }

            HashMap hashMap = new HashMap();
            hashMap.put("todayproids", nowproid);
            Log.e(TAG, "todayproids" + nowproid);
            responseEntity.setResult(com.alibaba.fastjson.JSON.toJSONString(hashMap));
        } else {
            Log.e(TAG, "当天节目全量接口null");
        }*/

        ArrayList<ProListVo> responseEntity1 = new ArrayList<>();
        for (int i = 0; i < todaylist.size(); i++) {
            ProListVo proListVo = new ProListVo();
            proListVo.setProgramId(todaylist.get(i).getId());
            proListVo.setTerminalIdentity(DeviceUtil.getUniqueID(AppContext.getInstance()));
            StringBuilder sb = new StringBuilder();
            List<ProgarmPalyPlan> okProgarms = todaylist.get(i).getPublicationPlanObject().getOkProgarms();
            for (int t = 0; t < okProgarms.size(); t++) {
                if (t > 0) {
                    sb.append("|");
                }
                ProgarmPalyPlan progarmPalyPlan = okProgarms.get(t);
                sb.append(progarmPalyPlan.getDuan());
            }
            proListVo.setTimeQuantum(sb.toString());
            proListVo.setType(0);
            responseEntity1.add(proListVo);
        }
        responseEntity.setResult(com.alibaba.fastjson.JSON.toJSONString(responseEntity1));
    }
}
