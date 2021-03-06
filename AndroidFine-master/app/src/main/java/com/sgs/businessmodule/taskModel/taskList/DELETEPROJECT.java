package com.sgs.businessmodule.taskModel.taskList;

import com.zhangke.zlog.ZLog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;

import java.util.ArrayList;

public class DELETEPROJECT extends TVTask {
    @Override
    public void runTv() {
        String dataJson = this.instructionRequest.getData();

        JSONObject jsonObject = JSON.parseObject(dataJson);

        JSONArray arrayListJson = (JSONArray) jsonObject.get("programId");

        ArrayList<Integer> arrayList = (ArrayList<Integer>) JSONObject.parseArray(arrayListJson.toJSONString(), Integer.class);

        ProgramScheduledManager.getInstance().clearLooperAndDBById(arrayList);

    }

    public static void main(String[] args) {

        String dataJson = "{\"programId\":[75]}\n";

        JSONObject jsonObject = JSON.parseObject(dataJson);

        JSONArray arrayList = (JSONArray) jsonObject.get("programId");

        ArrayList<Integer> list = (ArrayList<Integer>) JSONObject.parseArray(arrayList.toJSONString(), Integer.class);

        ZLog.e(TAG, "DELETEPROJECT:" + arrayList);

    }
}
