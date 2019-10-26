package com.dbModel.dao;

import android.content.Context;

import com.dbModel.DatabaseHelper;
import com.dbModel.entity.InstructionRequest;
import com.dbModel.entity.Order;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class InstructionRequestDao {
    private Context context;
    private Dao<InstructionRequest, Integer> OrderDaoOpe;
    private DatabaseHelper helper;

    public InstructionRequestDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            OrderDaoOpe = helper.getDao(Order.class);
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

}
