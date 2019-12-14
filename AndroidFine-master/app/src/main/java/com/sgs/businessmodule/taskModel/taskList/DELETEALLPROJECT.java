package com.sgs.businessmodule.taskModel.taskList;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;

public class DELETEALLPROJECT extends TVTask {
    @Override
    public void runTv() {
        ProgramScheduledManager.getInstance().clearLooperAndDBAndResource();
    }
}
