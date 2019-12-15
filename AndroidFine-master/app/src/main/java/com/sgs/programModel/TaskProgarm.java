package com.sgs.programModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.businessmodule.downloadModel.DownLoadManager;
import com.sgs.middle.utils.TextUtil;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

public class TaskProgarm {

    public static void progarmTest1(DownLoadManager manager) {
        progarmTest(manager, TextUtil.gettextAssets("nomal3.txt"));
    }

    public static String TAG = "TaskProgarm";

    static ProgarmPalyInstructionVo orderProgarmPalyInstructionVo;

    public static void progarmTest(DownLoadManager manager, String org) {

        String orgin = org;

        orderProgarmPalyInstructionVo = JSON.parseObject(orgin, new TypeReference<ProgarmPalyInstructionVo>() {
        });

        ProgramScheduledManager.getInstance().doProgarm(orderProgarmPalyInstructionVo, true);
    }
}
