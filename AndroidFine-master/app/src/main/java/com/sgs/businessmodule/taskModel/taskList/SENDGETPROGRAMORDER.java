package com.sgs.businessmodule.taskModel.taskList;

import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramDbManager;
import com.sgs.programModel.SendToServerUtil;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

import java.util.ArrayList;


public class SENDGETPROGRAMORDER extends TVTask {

    @Override
    public void runTv() {
        //从数据库中获取所有的节目数据
        ArrayList<ProgarmPalyInstructionVo> arrayList = (ArrayList<ProgarmPalyInstructionVo>) ProgramDbManager.getInstance().getAllProgarmPalyInstructionVo();
        SendToServerUtil.sendAddOrDelProList(arrayList, 1);
    }
}
