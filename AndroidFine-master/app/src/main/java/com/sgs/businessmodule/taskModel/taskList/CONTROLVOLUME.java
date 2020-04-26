package com.sgs.businessmodule.taskModel.taskList;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.middle.utils.StringUtil;

public class CONTROLVOLUME extends TVTask {
    @Override
    public void runTv() {
        Log.e(TAG, "CONTROLVOLUME:");

        String prog = super.instructionRequest.getData();

        JSONObject j2 = JSON.parseObject(prog);
        String condition = null;
        if (j2.containsKey("volumenum")) {
            condition = j2.getString("volumenum");
        }
        Log.e(TAG, "progJson:" + prog);
        isNeedSend = false;

        if (!StringUtil.isEmpty(condition)) {

            int index = Integer.parseInt(condition);

            CommandHelper.setStreamVolume(index, AppContext.getInstance());
        }
    }
}
