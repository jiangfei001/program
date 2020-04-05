package com.sgs.middle.dbModel.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.middle.dbModel.DatabaseHelper;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ScenceReportDao {
    private Context context;
    private Dao<ScenceReport, Integer> OrderDaoOpe;
    private DatabaseHelper helper;

    public ScenceReportDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            OrderDaoOpe = helper.getDao(ScenceReport.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @throws SQLException
     */
    public void add(ScenceReport instructionRequest) {
        try {
            OrderDaoOpe.createOrUpdate(instructionRequest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ScenceReport instructionRequest) {
        try {
            OrderDaoOpe.update(instructionRequest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ScenceReport queryByDateAndScenceId(int scenceId, String dateStr) {
        try {
            QueryBuilder<ScenceReport, Integer> queryBuilder = OrderDaoOpe.queryBuilder();
            queryBuilder.where().eq("scenceid", scenceId).and().eq("dateStr", dateStr);
            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<ScenceReport> queryByDate(String dateStr) {
        try {
            QueryBuilder<ScenceReport, Integer> queryBuilder = OrderDaoOpe.queryBuilder();
            queryBuilder.where().eq("dateStr", dateStr);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ScenceReport get(int id) {
        try {
            return OrderDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ScenceReport> getAllTask() {
        try {
            return OrderDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ScenceReport> byStatus() {
        try {
            return OrderDaoOpe.queryBuilder().where().notIn("status", 1, 2).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delByDate(String yesterday) {
        try {
            DeleteBuilder deleteBuilder = OrderDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("dateStr", yesterday);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
