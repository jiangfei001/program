package com.sgs.businessmodule.taskModel.taskList;

import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.zhangke.zlog.ZLog;

public class RESTARTCOMPUTER extends TVTask {
    @Override
    public void runTv() {
        ZLog.e(TAG, "RELEASEMUSIC:");
        CommandHelper.reboot(AppContext.getInstance());
    }
}
