package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.businessmodule.taskModel.TVTask;

import java.util.Date;


public class RELEASE extends TVTask {

    ProgarmPalyInstructionVo response;

    @Override
    public void runTv() {
        String prog = super.instructionRequest.getData();
        Log.e(TAG, "progJson:" + prog);
        isNeedSend = false;
        response = JSON.parseObject(prog, new TypeReference<ProgarmPalyInstructionVo>() {
        });
        response.setZlid(this.instructionRequest.getId());
        response.setReceiveTime(new Date());
        response.setExecuteTime(new Date());
        response.setType(this.instructionRequest.getType());
        ProgramScheduledManager.getInstance().doProgarm(response, true);
    }
}
