package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.SendToServerUtil;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

import java.util.ArrayList;

public class GETNEWPROGRAMMESSAGE extends TVTask {
    @Override
    public void runTv() {
        isNeedSend = false;
        Log.e(TAG, "GETNEWPROGRAMMESSAGE:");
        ProgarmPalyInstructionVo progarmPalyInstructionVo = ProgramScheduledManager.getInstance().programTaskManager.getNowProgarmPalyInstructionVo();
        ArrayList<ProgarmPalyInstructionVo> progarmPalyInstructionVos = new ArrayList<>();
        progarmPalyInstructionVos.add(progarmPalyInstructionVo);
        SendToServerUtil.sendNowPro(this.responseEntity, progarmPalyInstructionVos);
    }
}
