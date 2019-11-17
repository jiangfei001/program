package com.taskModel.taskList;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.commandModel.command.CommandHelper;
import com.dbModel.entity.InstructionRequest;
import com.programModel.entity.ProgarmPalyInstructionVo;
import com.taskModel.TVTask;

public class RELEASE extends TVTask {

    @Override
    public void runTv() {

        String prog = super.instructionRequest.getData();

        ProgarmPalyInstructionVo response = JSON.parseObject(prog, new TypeReference<ProgarmPalyInstructionVo>() {
        });
    }
}
