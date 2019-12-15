package com.sgs.businessmodule.taskModel.taskList;


import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;

public class SETOSTERMINAL extends TVTask {

    @Override
    public void runTv() {
        CommandHelper.openOrClose();
    }
}
