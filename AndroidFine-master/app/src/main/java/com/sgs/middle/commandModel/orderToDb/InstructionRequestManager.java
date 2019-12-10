package com.sgs.middle.commandModel.orderToDb;

import android.content.Context;

import com.sgs.middle.dbModel.dao.InstructionRequestDao;
import com.sgs.middle.dbModel.entity.InstructionRequest;
import com.sgs.AppContext;

public class InstructionRequestManager {
    private final String TAG = "DataManager";

    private Context context;

    private InstructionRequestManager(Context context) {
        this.context = context;
    }

    private static InstructionRequestManager instance;

    public static InstructionRequestManager getInstance() {
        if (instance == null) {
            synchronized (InstructionRequestManager.class) {
                if (instance == null) {
                    instance = new InstructionRequestManager(AppContext.getInstance());
                }
            }
        }
        return instance;
    }

    /**
     * 保存基础任务
     *
     * @param instructionRequest instructionRequest == null >> instructionRequest = new User();
     */
    public void saveInstructionRequest(InstructionRequest instructionRequest) {
        new InstructionRequestDao(context).add(instructionRequest);
    }
}
