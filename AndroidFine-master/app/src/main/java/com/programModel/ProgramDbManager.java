package com.programModel;

import android.content.Context;

import com.dbModel.dao.ProgrameDao;
import com.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.jfei.common.AppContext;

import java.util.List;

public class ProgramDbManager {
    private final String TAG = "ProgramDbManager";

    private Context context;

    private ProgramDbManager(Context context) {
        this.context = context;
    }

    private static ProgramDbManager instance;

    public static ProgramDbManager getInstance() {
        if (instance == null) {
            synchronized (ProgramDbManager.class) {
                if (instance == null) {
                    instance = new ProgramDbManager(AppContext.getInstance());
                }
            }
        }
        return instance;
    }

    /**
     * 保存基础任务
     *
     * @param progarmPalyInstructionVo instructionRequest == null >> instructionRequest = new User();
     */
    public void saveProgarmPalyInstructionVoRequest(ProgarmPalyInstructionVo progarmPalyInstructionVo) {
        new ProgrameDao(context).add(progarmPalyInstructionVo);
    }

    public List<ProgarmPalyInstructionVo> getAllProgarmPalyInstructionVo() {
        return new ProgrameDao(context).getAllProgarmPalyInstructionVo();
    }
}
