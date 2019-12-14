package com.sgs.middle.dbModel.dao;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.sgs.middle.dbModel.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

import java.sql.SQLException;
import java.util.List;

public class ProgrameDao {

    private Context context;
    private Dao<ProgarmPalyInstructionVo, Integer> programeDaoOpe;
    private DatabaseHelper helper;

    public ProgrameDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            programeDaoOpe = helper.getDao(ProgarmPalyInstructionVo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @throws SQLException
     */
    public void add(ProgarmPalyInstructionVo progarmPalyInstructionVo) {
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
            programeDaoOpe.createOrUpdate(progarmPalyInstructionVo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ProgarmPalyInstructionVo get(int id) {
        try {
            return programeDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ProgarmPalyInstructionVo> getAllProgarmPalyInstructionVo() {
        try {
            return programeDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int delAllProgarmPalyInstructionVo() {
        try {
            DeleteBuilder<ProgarmPalyInstructionVo, Integer> deleteBuilder = programeDaoOpe.deleteBuilder();
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int delByProgarmPalyInstructionVoID(int id) {
        try {
            DeleteBuilder<ProgarmPalyInstructionVo, Integer> deleteBuilder = programeDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("id", id);
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
