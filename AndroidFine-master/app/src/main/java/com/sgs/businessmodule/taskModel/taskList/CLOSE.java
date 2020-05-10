package com.sgs.businessmodule.taskModel.taskList;


import android.util.Log;

import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.sgs.businessmodule.taskModel.TVTask;

public class CLOSE extends TVTask {

    @Override
    public void runTv() {
        Log.e(TAG, "CLOSE:");
        CommandHelper.openOrClose(false);
    }
}
