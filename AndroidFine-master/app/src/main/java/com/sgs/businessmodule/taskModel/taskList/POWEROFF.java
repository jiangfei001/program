package com.sgs.businessmodule.taskModel.taskList;


import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;

import com.sgs.businessmodule.taskModel.TVTask;

public class POWEROFF extends TVTask {
    @Override
    public void runTv() {
        CommandHelper.openOrClose();
    }
}
