package com.sgs.businessmodule.taskModel.taskList;


import com.sgs.middle.commandModel.command.CommandHelper;
import com.sgs.businessmodule.taskModel.TVTask;

public class DISPATCHERRELEASE extends TVTask {
    @Override
    public void runTv() {
        CommandHelper.deleteDir("");
    }
}
