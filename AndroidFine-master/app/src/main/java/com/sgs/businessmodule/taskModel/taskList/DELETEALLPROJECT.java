package com.sgs.businessmodule.taskModel.taskList;

import com.zhangke.zlog.ZLog;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;

public class DELETEALLPROJECT extends TVTask {
    @Override
    public void runTv() {
        ZLog.e(TAG, "DELETEALLPROJECT:");
        ProgramScheduledManager.getInstance().clearLooperAndDBAndResource();
    }
}
