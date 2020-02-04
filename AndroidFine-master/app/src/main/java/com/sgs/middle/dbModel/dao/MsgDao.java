package com.sgs.middle.dbModel.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg;
import com.sgs.middle.dbModel.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

public class MsgDao {

    private Context context;
    private Dao<MuTerminalMsg, Integer> muTerminalMsgDaoOpe;
    private DatabaseHelper helper;

    public MsgDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            muTerminalMsgDaoOpe = helper.getDao(MuTerminalMsg.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @throws SQLException
     */
    public void add(MuTerminalMsg progarmPalyInstructionVo) {
        /*//事务操作
		TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>()
				{
					@OverrideClass class com.sgs.programModel.entity.ProgarmPalyInstructionVo does not have an id field
					public Void call() throws Exception
					{
						return null;
					}
				});*/
        try {
            muTerminalMsgDaoOpe.createOrUpdate(progarmPalyInstructionVo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public MuTerminalMsg get(int id) {
        try {
            return muTerminalMsgDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MuTerminalMsg> getAllMuTerminalMsg() {
        try {
            return muTerminalMsgDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int delAllMuTerminalMsg() {
        try {
            DeleteBuilder<MuTerminalMsg, Integer> deleteBuilder = muTerminalMsgDaoOpe.deleteBuilder();
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int delByMuTerminalMsgID(int id) {
        try {
            DeleteBuilder<MuTerminalMsg, Integer> deleteBuilder = muTerminalMsgDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("id", id);
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
