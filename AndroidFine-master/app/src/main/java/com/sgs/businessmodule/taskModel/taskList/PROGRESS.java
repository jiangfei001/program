package com.sgs.businessmodule.taskModel.taskList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgramResource;
import com.zhangke.zlog.ZLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PROGRESS extends TVTask {
    @Override
    public void runTv() {
        ZLog.e(TAG, "FTPPUTPOLICY:");
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

        JSONObject jsonObject = JSON.parseObject(dataJson);

        try {
            int programId = (int) jsonObject.get("programId");


            String programName = (String) jsonObject.get("programName");

            String instructionId = (String) jsonObject.get("instructionId");

            ZLog.e("PROGRESS", "programId:" + programId + "programName:" + programName + "instructionId:" + instructionId);

            Iterator iterator = ProgramScheduledManager.getInstance().alllist.iterator();

            while (iterator.hasNext()) {
                //如果是ProgramZip
                ProgarmPalyInstructionVo response1 = (ProgarmPalyInstructionVo) iterator.next();
                ZLog.e("PROGRESS", "response1:" + response1.getProgramName() + response1.getId());

                if (response1.getId() == programId) {
                    //获取zip是否下载完成
                    hashMap.put("zipStatus", response1.getProgramZipStatus() + "");
                    if (response1.getProgramResourceListArray() != null && response1.getProgramResourceListArray().size() > 0) {
                        ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < response1.getProgramResourceListArray().size(); i++) {
                            ProgramResource programResource = response1.getProgramResourceListArray().get(i);
                            arrayList.add("fileName:" + programResource.getFileName() + "|url:" + programResource.getUrl() + "|downStaus:" + programResource.getDownStatus() + "|");
                        }
                        hashMap.put("ProgramResourceList", arrayList);
                    }

                    if (response1.getProgramMusicListArray() != null && response1.getProgramMusicListArray().size() > 0) {
                        for (int i = 0; i < response1.getProgramMusicListArray().size(); i++) {
                            ArrayList arrayList = new ArrayList();
                            ProgramResource programResource = response1.getProgramMusicListArray().get(i);
                            arrayList.add("fileName:" + programResource.getFileName() + "|url:" + programResource.getUrl() + "|downStaus:" + programResource.getDownStatus());
                            hashMap.put("ProgramMusicList", arrayList);
                        }
                    }
                    ZLog.e("PROGRESS", com.alibaba.fastjson.JSON.toJSONString(hashMap) + "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZLog.e("PROGRESS",e.getMessage());
            return;
        }
    }

    HashMap hashMap = new HashMap();

    @Override
    public void setResult() {
        responseEntity.setResult(com.alibaba.fastjson.JSON.toJSONString(hashMap));
    }
}