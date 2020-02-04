package com.sgs.businessmodule.taskUtil.cutMsg;

import android.content.Context;

import com.sgs.AppContext;
import com.sgs.middle.dbModel.dao.MsgDao;

import java.util.List;

public class MsgDbManager {
    private final String TAG = "ProgramDbManager";

    private Context contextdeletedownloader;

    private Context context;

    private MsgDbManager(Context context) {
        this.context = context;
    }

    private static MsgDbManager instance;

    public static MsgDbManager getInstance() {
        if (instance == null) {
            synchronized (MsgDbManager.class) {
                if (instance == null) {
                    instance = new MsgDbManager(AppContext.getInstance());
                }
            }
        }
        return instance;
    }

    /**
     * 保存基础任务
     *
     * @param muTerminalMsg instructionRequest == null >> instructionRequest = new User();
     */
    public void saveMuTermianlMsg(MuTerminalMsg muTerminalMsg) {
        new MsgDao(context).add(muTerminalMsg);
    }


    public void delByMuTerminalMsgID(int id) {
        new MsgDao(context).delByMuTerminalMsgID(id);
    }

    public MuTerminalMsg getMuTerminalMsgById(int id) {
        return new MsgDao(context).get(id);
    }

    public void delAllMuTerminalMsg() {
        new MsgDao(context).delAllMuTerminalMsg();
    }


    public List<MuTerminalMsg> getAllMuTerminalMsg() {
        return new MsgDao(context).getAllMuTerminalMsg();
    }
}
