package com.taskModel.taskList;


import com.commandModel.command.CommandHelper;
import com.taskModel.BasicTask;
import com.taskModel.TVTask;

public class DISPATCHERRELEASE extends TVTask {
    @Override
    public void runTv() {
        CommandHelper.deleteDir("");
    }
}
