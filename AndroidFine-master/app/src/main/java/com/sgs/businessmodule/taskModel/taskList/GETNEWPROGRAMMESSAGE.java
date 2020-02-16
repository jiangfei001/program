package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

import java.util.ArrayList;
import java.util.HashMap;

public class GETNEWPROGRAMMESSAGE extends TVTask {

    ArrayList<Integer> progarmPalyInstructionVos;

    @Override
    public void runTv() {
        Log.e(TAG, "GETNEWPROGRAMMESSAGE:");
        progarmPalyInstructionVos = new ArrayList<>();
        ProgarmPalyInstructionVo progarmPalyInstructionVo1 = ProgramScheduledManager.getInstance().programTaskManager.getNowProgarmPalyInstructionVo();
        if (progarmPalyInstructionVo1 != null) {
            Log.e(TAG, "GETNEWPROGRAMMESSAGE:我不为空");
            progarmPalyInstructionVos.add(progarmPalyInstructionVo1.getId());
        }
        // SendToServerUtil.sendNowPro(this.responseEntity, progarmPalyInstructionVos);
    }

    @Override
    public void setResult() {
        if (progarmPalyInstructionVos != null) {
            Log.e(TAG, "当天节目全量接口" + progarmPalyInstructionVos.size());

            StringBuilder nowproid = new StringBuilder();

            for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
                if (i != 0) {
                    nowproid.append(",");
                }
                nowproid.append(progarmPalyInstructionVos.get(i));
            }

            HashMap hashMap = new HashMap();
            hashMap.put("nowproids", nowproid);
            Log.e(TAG, "nowproids" + nowproid);
            responseEntity.setResult(com.alibaba.fastjson.JSON.toJSONString(hashMap));
        } else {
            Log.e(TAG, "当天节目全量接口null");
        }
    }
}
