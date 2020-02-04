package com.sgs.businessmodule.taskModel.taskList;


import android.util.Log;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskUtil.cutMsg.MsgDbManager;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalBack;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CLEARCUTMSG extends TVTask {


    ArrayList<MuTerminalBack> muTerminalMsgs = new ArrayList<>();

    @Override
    public void runTv() {
        Log.e(TAG, "CLEARCUTMSG:");
        ArrayList<com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg> muTerminalMsgs1 = (ArrayList<MuTerminalMsg>) MsgDbManager.getInstance().getAllMuTerminalMsg();
        for (int i = 0; i < muTerminalMsgs1.size(); i++) {
            MuTerminalBack muTerminalBack = new MuTerminalBack();
            muTerminalBack.setMsgStatus("0");
            muTerminalBack.setMsgId(muTerminalMsgs1.get(i).getId());
            muTerminalBack.setFinishTime(muTerminalMsgs1.get(i).getHasplay());
            muTerminalMsgs.add(muTerminalBack);
        }

        Event event = new Event();
        event.setId(EventEnum.EVENT_TEST_SETCLEARCUTMSG);
        EventBus.getDefault().post(event);
    }

    @Override
    public void setResult() {
        responseEntity.setResult(com.alibaba.fastjson.JSON.toJSONString(muTerminalMsgs));
    }
}
