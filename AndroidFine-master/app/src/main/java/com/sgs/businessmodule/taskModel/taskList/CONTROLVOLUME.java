package com.sgs.businessmodule.taskModel.taskList;


import android.util.Log;

import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.TVTask;

public class CONTROLVOLUME extends TVTask {
    @Override
    public void runTv() {
        Log.e(TAG, "CONTROLVOLUME:");
        CommandHelper.setStreamVolume(100, AppContext.getInstance());
    }
}
