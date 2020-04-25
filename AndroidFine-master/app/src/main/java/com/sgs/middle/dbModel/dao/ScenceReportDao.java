package com.sgs.middle.dbModel.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.middle.dbModel.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

public class ScenceReportDao {
    private static final String TAG = "ScenceReportDao";
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

    public ScenceReport queryByDateAndScenceId(int sceneId, String palyDate) {
        try {
            QueryBuilder<ScenceReport, Integer> queryBuilder = OrderDaoOpe.queryBuilder();
            queryBuilder.where().eq("sceneId", sceneId).and().eq("palyDate", palyDate);
            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<ScenceReport> queryByDate(String palyDate) {
        try {
            QueryBuilder<ScenceReport, Integer> queryBuilder = OrderDaoOpe.queryBuilder();
            queryBuilder.where().eq("palyDate", palyDate);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ScenceReport> queryByNotToday(String palyDate) {
        try {
            QueryBuilder<ScenceReport, Integer> queryBuilder = OrderDaoOpe.queryBuilder();
            queryBuilder.where().ne("palyDate", palyDate);
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
            deleteBuilder.where().eq("palyDate", yesterday);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delByNotToday(String yesterday) {
        try {
            DeleteBuilder deleteBuilder = OrderDaoOpe.deleteBuilder();
            deleteBuilder.where().ne("palyDate", yesterday);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delOneMouthAgo(String lastOneMouth) {
        try {
            DeleteBuilder deleteBuilder = OrderDaoOpe.deleteBuilder();
            deleteBuilder.where().le("palyDate", lastOneMouth);
            int num = deleteBuilder.delete();
            Log.e(TAG, "num" + num);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int delList(List<ScenceReport> scenceReports) {
        try {
            return OrderDaoOpe.delete(scenceReports);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
