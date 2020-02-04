package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskUtil.cutMsg.MsgDbManager;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class CUTMSG extends TVTask {

    @Override
    public void runTv() {
        Log.e(TAG, "CLOSE:");
        String prog = super.instructionRequest.getData();
        Log.e(TAG, "progJson:" + prog);
        MuTerminalMsg muTerminalMsg = JSON.parseObject(prog, new TypeReference<MuTerminalMsg>() {
        });

        Event event = new Event();
        HashMap<EventEnum, Object> params = new HashMap();
        params.put(EventEnum.EVENT_TEST_MSG1_KEY_CUTMSG, muTerminalMsg);
        event.setParams(params);
        event.setId(EventEnum.EVENT_TEST_SETCUTMSG);
        EventBus.getDefault().post(event);
        System.out.println("muTerminalMsg.toString()" + muTerminalMsg.toString());
    }
}
