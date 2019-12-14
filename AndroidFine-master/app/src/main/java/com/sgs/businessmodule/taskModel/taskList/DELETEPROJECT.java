package com.sgs.businessmodule.taskModel.taskList;

import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;

public class DELETEPROJECT extends TVTask {
    @Override
    public void runTv() {
        CommandHelper.deleteDir("");
        ProgramScheduledManager.getInstance().clearLooperAndDBById(12);
    }
}
