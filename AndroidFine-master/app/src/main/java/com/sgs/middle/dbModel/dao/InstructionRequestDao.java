package com.sgs.middle.dbModel.dao;

import android.content.Context;

import com.sgs.middle.dbModel.DatabaseHelper;
import com.sgs.middle.dbModel.entity.InstructionRequest;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class InstructionRequestDao {
    private Context context;
    private Dao<InstructionRequest, Integer> OrderDaoOpe;
    private DatabaseHelper helper;

    public InstructionRequestDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            OrderDaoOpe = helper.getDao(InstructionRequest.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @throws SQLException
     */
    public void add(InstructionRequest instructionRequest) {
        /*//事务操作
		TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>()
				{

					@Override
					public Void call() throws Exception
					{
						return null;
					}
				});*/
        try {
            OrderDaoOpe.createOrUpdate(instructionRequest);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public InstructionRequest get(int id) {
        try {
            return OrderDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InstructionRequest> getAllTask() {
        try {
            return OrderDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InstructionRequest> byStatus() {
        try {
            return OrderDaoOpe.queryBuilder().where().notIn("status",1,2).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
