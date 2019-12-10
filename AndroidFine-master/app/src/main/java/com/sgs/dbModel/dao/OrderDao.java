package com.sgs.dbModel.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.sgs.dbModel.DatabaseHelper;
import com.sgs.dbModel.entity.Order;

import java.sql.SQLException;

public class OrderDao {
    private Context context;
    private Dao<Order, Integer> OrderDaoOpe;
    private DatabaseHelper helper;

    public OrderDao(Context context) {
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
     * @param Order
     * @throws SQLException
     */
    public void add(Order Order) {
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
            OrderDaoOpe.createOrUpdate(Order);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Order get(int id) {
        try {
            return OrderDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
