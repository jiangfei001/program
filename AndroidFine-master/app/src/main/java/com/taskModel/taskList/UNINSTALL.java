package com.taskModel.taskList;


import com.alibaba.fastjson.JSON;
import com.commandModel.command.CommandHelper;
import com.sgs.jfei.common.AppContext;
import com.taskModel.TVTask;

import java.util.HashMap;

public class UNINSTALL extends TVTask {
    //"code":125,"data":"{'':'dfdsa.apk'}","id":55,"priority":1,"terminalIdentity":"jf","type":"uninstall"
    @Override
    public void runTv() {
        String dataJson = this.instructionRequest.getData();
        HashMap<String, String> maps2 = new HashMap<>();
        try {
            maps2 = (HashMap) JSON.parse(dataJson);
        } catch (Exception e) {
        }
        if (maps2.get("apkPageName") != null) {
            CommandHelper.startUninstall(maps2.get("apkPageName"), AppContext.getInstance());
        }
    }
}
