package com.sgs.businessmodule.taskModel.taskList;

import com.zhangke.zlog.ZLog;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

import java.util.ArrayList;
import java.util.HashMap;

public class GETNEWPROGRAMMESSAGE extends TVTask {

    ArrayList<Integer> progarmPalyInstructionVos;

    @Override
    public void runTv() {
        ZLog.e(TAG, "GETNEWPROGRAMMESSAGE:");
        progarmPalyInstructionVos = new ArrayList<>();
        ProgarmPalyInstructionVo progarmPalyInstructionVo1 = ProgramScheduledManager.getInstance().programTaskManager.getNowProgarmPalyInstructionVo();
        if (progarmPalyInstructionVo1 != null) {
            ZLog.e(TAG, "GETNEWPROGRAMMESSAGE:我不为空");
            progarmPalyInstructionVos.add(progarmPalyInstructionVo1.getId());
        }
        // SendToServerUtil.sendNowPro(this.responseEntity, progarmPalyInstructionVos);
    }

    @Override
    public void setResult() {
        if (progarmPalyInstructionVos != null) {
            ZLog.e(TAG, "当天节目全量接口" + progarmPalyInstructionVos.size());

            StringBuilder nowproid = new StringBuilder();

            for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
                if (i != 0) {
                    nowproid.append(",");
                }
                nowproid.append(progarmPalyInstructionVos.get(i));
            }

            HashMap hashMap = new HashMap();
            hashMap.put("nowproids", nowproid);
            ZLog.e(TAG, "nowproids" + nowproid);
            responseEntity.setResult(com.alibaba.fastjson.JSON.toJSONString(hashMap));
        } else {
            ZLog.e(TAG, "当天节目全量接口null");
        }
    }
}
