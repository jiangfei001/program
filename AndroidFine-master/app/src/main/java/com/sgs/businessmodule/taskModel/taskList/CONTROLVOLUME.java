package com.sgs.businessmodule.taskModel.taskList;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.middle.utils.SharedPreferences;
import com.sgs.middle.utils.StringUtil;
import com.sgs.middle.utils.UsageStatsManagerUtil;
import com.sgs.programModel.ProgramUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CONTROLVOLUME extends TVTask {

    public static String dingshi = "cvds";

    @Override
    public void runTv() {
        Log.e(TAG, "CONTROLVOLUME:");

        String prog = super.instructionRequest.getData();

        JSONObject j2 = JSON.parseObject(prog);
        String volumenum = null;
        if (j2.containsKey("volumenum")) {
            volumenum = j2.getString("volumenum");
        }
        String taskVolumeTime = "";
        if (j2.containsKey("taskVolumeTime")) {
            taskVolumeTime = j2.getString("taskVolumeTime");
        }
        List<String> list = null;
        if (j2.containsKey("weekList2")) {
            String weekList = j2.getString("weekList2");
            list = JSONObject.parseArray(weekList, String.class);
            Log.e("list", list.size() + "");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        }

        Log.e(TAG, "progJson:" + prog);

        if (!StringUtil.isEmpty(volumenum) && StringUtil.isEmpty(taskVolumeTime)) {
            int index = Integer.parseInt(volumenum);
            CommandHelper.setStreamVolume(index, AppContext.getInstance());
        } else {
            //计算时间进行定时
            Date date = new Date();
            String xinqi = ProgramUtil.getWeekOfDate(date);
            if (j2.containsKey("weekList2") && list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (xinqi.equals(list.get(i))) {
                        //定时音量
                        UsageStatsManagerUtil.alarmcv(taskVolumeTime, volumenum);
                    }
                }
            }
            //存库，执行定时操作
            SharedPreferences.getInstance().putString(dingshi, prog);
        }
    }
}
