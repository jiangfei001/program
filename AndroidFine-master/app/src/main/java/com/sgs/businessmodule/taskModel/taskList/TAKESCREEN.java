package com.sgs.businessmodule.taskModel.taskList;


import com.sgs.AppContext;
import com.sgs.businessmodule.qiniuModel.QiniuUpHelper;
import com.sgs.businessmodule.taskModel.TVTask;

public class TAKESCREEN extends TVTask {
    @Override
    public void runTv() {
        QiniuUpHelper.upload(AppContext.getInstance().getNowActivity(), false);
    }
}