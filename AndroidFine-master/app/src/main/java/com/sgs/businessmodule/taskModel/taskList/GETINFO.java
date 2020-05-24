package com.sgs.businessmodule.taskModel.taskList;

import com.zhangke.zlog.ZLog;

import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;

public class GETINFO extends TVTask {
    @Override
    public void runTv() {
        ZLog.e(TAG, "GETINFO:");
        String hardwareinfo = CommandHelper.trackHardwareInfo();
    }
}
