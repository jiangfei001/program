package com.sgs.businessmodule.taskModel.taskList;


import com.zhangke.zlog.ZLog;

import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.sgs.businessmodule.taskModel.TVTask;

public class CLOSE extends TVTask {

    @Override
    public void runTv() {
        ZLog.e(TAG, "CLOSE:");
        CommandHelper.openOrClose(false);
    }
}
