package com.sgs.programModel;

import android.content.Context;

import com.sgs.middle.dbModel.dao.ProgrameDao;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.AppContext;

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


    public void delectProgarmPalyInstructionVoRequestById(int id) {
        new ProgrameDao(context).delByProgarmPalyInstructionVoID(id);
    }

    public void delectAllProgarmPalyInstructionVoRequest() {
        new ProgrameDao(context).delAllProgarmPalyInstructionVo();
    }


    public List<ProgarmPalyInstructionVo> getAllProgarmPalyInstructionVo() {
        return new ProgrameDao(context).getAllProgarmPalyInstructionVo();
    }
}
