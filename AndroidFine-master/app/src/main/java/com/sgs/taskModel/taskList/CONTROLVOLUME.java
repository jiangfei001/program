package com.sgs.taskModel.taskList;


import com.sgs.commandModel.command.CommandHelper;
import com.sgs.AppContext;
import com.sgs.taskModel.TVTask;

public class CONTROLVOLUME extends TVTask {
    @Override
    public void runTv() {
        CommandHelper.setStreamVolume(100, AppContext.getInstance());
    }
}
