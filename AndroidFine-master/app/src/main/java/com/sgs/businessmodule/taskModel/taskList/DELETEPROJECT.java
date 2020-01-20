package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;

import java.util.ArrayList;

public class DELETEPROJECT extends TVTask {
    @Override
    public void runTv() {
        Log.e(TAG, "DELETEPROJECT:");
        String dataJson = this.instructionRequest.getData();
        ArrayList<Integer> arrayList = new ArrayList<>();
        try {
            arrayList = (ArrayList<Integer>) JSON.parse(dataJson);
        } catch (Exception e) {
        }
       /* if (maps2.get("apkPageName") != null) {
            CommandHelper.startUninstall(maps2.get("apkPageName"), AppContext.getInstance());
        }
*/
        ProgramScheduledManager.getInstance().clearLooperAndDBById(arrayList);

    }
}
