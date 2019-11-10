package com.taskModel.taskList;


import com.commandModel.command.CommandHelper;
import com.sgs.jfei.common.AppContext;
import com.taskModel.TVTask;

public class CONTROLVOLUME extends TVTask {
    @Override
    public void runTv() {
        CommandHelper.setStreamVolume(100, AppContext.getInstance());
    }
}
