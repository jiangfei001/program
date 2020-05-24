package com.sgs.businessmodule.taskModel.taskList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgramResource;
import com.sgs.programModel.taskUtil.MyTask;
import com.zhangke.zlog.ZLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RUNDATA extends TVTask {
    @Override
    public void runTv() {
       /* 终端运行状态的josn内容：
        包括：终端节目列表
        终端：运行中的节目
                终端3个节目播放队列的情况
        终端插播消息的情况
        可以看到终端运行的情况数据，辅助分析问题*/

        /*{
            "data":[],
            "terminalIdentity":"078a0551-b333-323c-a09d-4af272baa82a",
                "type":0
        }*/

        /*weboStcok通讯
        当前部分对应的是WebStock指令中的 instructionJson 内容*/

       /* 名称	类型	非空	描述
        programId	int	Y	节目Id
        programName	String	Y	节目名称
        instructionId	int	Y	"原下发节目的指令ID，
        同一个节目下发多次，会有多个下载记录，下发的指令ID是唯一的"*/

        String dataJson = this.instructionRequest.getData();


        ArrayList<MyTask> lunxuntask = (ArrayList<MyTask>) ProgramScheduledManager.getInstance().programTaskManager.getMyTaskTimeTask().getdTasks();

        ArrayList<MyTask> zhouqitask = (ArrayList<MyTask>) ProgramScheduledManager.getInstance().programTaskManager.getMyTaskTimeTask().getmTasks();

        ArrayList<MyTask> duzhantask = (ArrayList<MyTask>) ProgramScheduledManager.getInstance().programTaskManager.getMyTaskTimeTask().getPriorsTasks();

        hashMap.put("lunxuntask", com.alibaba.fastjson.JSON.toJSONString(lunxuntask));
        hashMap.put("zhouqitask", com.alibaba.fastjson.JSON.toJSONString(zhouqitask));
        hashMap.put("duzhantask", com.alibaba.fastjson.JSON.toJSONString(duzhantask));

        ZLog.e("RUNDATA",com.alibaba.fastjson.JSON.toJSONString(hashMap));
    }


    HashMap hashMap = new HashMap();

    @Override
    public void setResult() {
        responseEntity.setResult(com.alibaba.fastjson.JSON.toJSONString(hashMap));
    }
}