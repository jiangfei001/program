package com.sgs.businessmodule.taskModel.taskList;

import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;

public class GETINFO extends TVTask {
    @Override
    public void runTv() {
        String hardwareinfo = CommandHelper.trackHardwareInfo();
    }
}
