package com.taskModel.taskList;


import com.commandModel.command.CommandHelper;
import com.taskModel.TVTask;

public class CLOSE extends TVTask {

    @Override
    public void runTv() {
        CommandHelper.openOrClose();
    }
}
