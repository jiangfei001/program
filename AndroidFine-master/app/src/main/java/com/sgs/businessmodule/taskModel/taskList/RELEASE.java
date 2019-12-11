package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.businessmodule.taskModel.TVTask;


public class RELEASE extends TVTask {

    ProgarmPalyInstructionVo response;

    @Override
    public void runTv() {

        String prog = super.instructionRequest.getData();
        Log.e(TAG, "progJson:" + prog);

        response = JSON.parseObject(prog, new TypeReference<ProgarmPalyInstructionVo>() {
        });

        ProgramScheduledManager.getInstance().doProgarm(response, true);
    }
}
