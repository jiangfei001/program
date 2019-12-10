package com.sgs.programModel.taskUtil;

import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

public class MyTask extends Task {
    public String name;

    public ProgarmPalyInstructionVo progarmPalyInstructionVo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgarmPalyInstructionVo getProgarmPalyInstructionVo() {
        return progarmPalyInstructionVo;
    }

    public void setProgarmPalyInstructionVo(ProgarmPalyInstructionVo progarmPalyInstructionVo) {
        this.progarmPalyInstructionVo = progarmPalyInstructionVo;
    }


}
