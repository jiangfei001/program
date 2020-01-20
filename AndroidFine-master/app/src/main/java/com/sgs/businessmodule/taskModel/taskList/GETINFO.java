package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;

public class GETINFO extends TVTask {
    @Override
    public void runTv() {
        Log.e(TAG, "GETINFO:");
        String hardwareinfo = CommandHelper.trackHardwareInfo();
    }
}
