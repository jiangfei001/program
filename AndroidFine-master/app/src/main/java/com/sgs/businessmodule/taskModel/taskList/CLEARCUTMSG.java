package com.sgs.businessmodule.taskModel.taskList;


import android.util.Log;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;

import org.greenrobot.eventbus.EventBus;

public class CLEARCUTMSG extends TVTask {

    @Override
    public void runTv() {
        Log.e(TAG, "CLEARCUTMSG:");
        Event event = new Event();
        event.setId(EventEnum.EVENT_TEST_SETCLEARCUTMSG);
        EventBus.getDefault().post(event);
    }
}
