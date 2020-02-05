package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskUtil.cutMsg.MsgDbManager;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalBack;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

public class DELETECUTMSG extends TVTask {

    ArrayList<MuTerminalBack> muTerminalMsgs = new ArrayList<>();

    @Override
    public void runTv() {
        String dataJson = this.instructionRequest.getData();
        Log.e(TAG, "dataJson:" + dataJson);
        try {
            JSONObject jsonObject = JSON.parseObject(dataJson);
            String arrayListJson1 = (String) jsonObject.get("ids");
            Log.e(TAG, "dataJson:" + arrayListJson1);
            ArrayList<Integer> arrayList = (ArrayList<Integer>) JSONObject.parseArray(arrayListJson1, Integer.class);

            for (int i = 0; i < arrayList.size(); i++) {
                MuTerminalMsg muTerminalMsg = MsgDbManager.getInstance().getMuTerminalMsgById(arrayList.get(i));
                if (muTerminalMsg != null) {
                    MuTerminalBack muTerminalBack = new MuTerminalBack();
                    muTerminalBack.setMsgStatus("2");
                    muTerminalBack.setMsgId(muTerminalMsg.getId());
                    muTerminalBack.setFinishTime(muTerminalMsg.getHasplay());
                    muTerminalMsgs.add(muTerminalBack);
                }
            }

            Event event = new Event();
            HashMap<EventEnum, Object> params = new HashMap();
            params.put(EventEnum.EVENT_TEST_MSG1_KEY_DELETECUTMSG, arrayList);
            event.setParams(params);
            event.setId(EventEnum.EVENT_TEST_DELETECUTMSG);
            EventBus.getDefault().post(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setResult() {
        responseEntity.setResult(com.alibaba.fastjson.JSON.toJSONString(muTerminalMsgs));
    }

}
