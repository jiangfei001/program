package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;
import com.sgs.programModel.ProgramScheduledManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

public class DELETECUTMSG extends TVTask {

    @Override
    public void runTv() {
        String dataJson = this.instructionRequest.getData();

        Log.e(TAG, "dataJson:" + dataJson);
        JSONObject jsonObject = JSON.parseObject(dataJson);

        JSONArray arrayListJson = (JSONArray) jsonObject.get("ids");

        ArrayList<Integer> arrayList = (ArrayList<Integer>) JSONObject.parseArray(arrayListJson.toJSONString(), Integer.class);

        Event event = new Event();
        HashMap<EventEnum, Object> params = new HashMap();
        params.put(EventEnum.EVENT_TEST_MSG1_KEY_DELETECUTMSG, arrayList);
        event.setParams(params);
        event.setId(EventEnum.EVENT_TEST_DELETECUTMSG);
        EventBus.getDefault().post(event);

    }
}
