package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;

public class DELETEALLPROJECT extends TVTask {
    @Override
    public void runTv() {
        Log.e(TAG, "DELETEALLPROJECT:");
        ProgramScheduledManager.getInstance().clearLooperAndDBAndResource();
    }
}
