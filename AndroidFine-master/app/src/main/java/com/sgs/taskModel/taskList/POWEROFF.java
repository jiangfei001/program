package com.sgs.taskModel.taskList;


import com.sgs.commandModel.command.CommandHelper;

import com.sgs.taskModel.TVTask;

public class POWEROFF extends TVTask {
    @Override
    public void runTv() {
        CommandHelper.openOrClose();
    }
}
