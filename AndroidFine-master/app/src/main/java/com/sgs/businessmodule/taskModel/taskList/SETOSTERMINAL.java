package com.sgs.businessmodule.taskModel.taskList;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.sgs.middle.receiver.CustomAlarmReceiver;
import com.sgs.middle.utils.SharedPreferences;
import com.sgs.middle.utils.StringUtil;
import com.sgs.middle.utils.UsageStatsManagerUtil;
import com.sgs.programModel.ProgramUtil;

import java.util.Date;
import java.util.List;

public class SETOSTERMINAL extends TVTask {
    public static String SETOSTERMINAL = "SETOSTERMINAL";

    @Override
    public void runTv() {
        Log.e(TAG, "SETOSTERMINAL:");
        String prog = super.instructionRequest.getData();
        // "ids":["38"],"taskCard":"osTab","taskVolumeTime":"00:00:05","volumenum":56,"weekList2":["星期二"],"openTime":"00:04:04","shuntDownTime":"00:00:05","weekList":["星期二","星期三","星期四"]
        //存库，执行定时操作
        if (!StringUtil.isEmpty(prog)) {
            SharedPreferences.getInstance().putString(SETOSTERMINAL, prog);
            CustomAlarmReceiver.setco();
        }
        Log.e(TAG, "progJson:" + prog);
        // CommandHelper.openOrClose();
    }
}
