package com.sgs.businessmodule.taskModel.taskList;


import com.sgs.middle.commandModel.command.CommandHelper;
import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;

public class CONTROLVOLUME extends TVTask {
    @Override
    public void runTv() {
        CommandHelper.setStreamVolume(100, AppContext.getInstance());
    }
}
