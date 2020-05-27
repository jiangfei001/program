package com.sgs.middle.dbModel.dao;

import android.content.Context;
import com.zhangke.zlog.ZLog;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sgs.businessmodule.upReportModel.RepHotReport;
import com.sgs.middle.dbModel.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

public class RedHotReportDao {
    private static final String TAG = "RedHotReportDao";
    private Context context;
    private Dao<RepHotReport, Integer> OrderDaoOpe;
    private DatabaseHelper helper;

    public RedHotReportDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            OrderDaoOpe = helper.getDao(RepHotReport.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @throws SQLException
     */
    public void add(RepHotReport instructionRequest) {
        try {
            OrderDaoOpe.createOrUpdate(instructionRequest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(RepHotReport instructionRequest) {
        try {
            OrderDaoOpe.update(instructionRequest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RepHotReport queryByDateAndScenceId(int sceneId, String createTime, String areaName, String pageName) {
        try {
            QueryBuilder<RepHotReport, Integer> queryBuilder = OrderDaoOpe.queryBuilder();
            queryBuilder.where().eq("sceneId", sceneId).and().eq("createTime", createTime).and().eq("areaName", areaName).and().eq("pageName", pageName);
            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<RepHotReport> queryByDate(String palyDate) {
        try {
            QueryBuilder<RepHotReport, Integer> queryBuilder = OrderDaoOpe.queryBuilder();
            queryBuilder.where().eq("startTime", palyDate);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RepHotReport get(int id) {
        try {
            return OrderDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RepHotReport> getAllTask() {
        try {
            return OrderDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int delList(List<RepHotReport> repHotReports) {
        try {
            return OrderDaoOpe.delete(repHotReports);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<RepHotReport> byStatus() {
        try {
            return OrderDaoOpe.queryBuilder().where().notIn("status", 1, 2).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delByDate(String startTime) {
        try {
            DeleteBuilder deleteBuilder = OrderDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("startTime", startTime);
            int num = deleteBuilder.delete();
            ZLog.e(TAG, "num" + num);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delByNotToday(String startTime) {
        try {
            DeleteBuilder deleteBuilder = OrderDaoOpe.deleteBuilder();
            deleteBuilder.where().ne("startTime", startTime);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delOneMouthAgo(String oneMouthAgo) {
        try {
            DeleteBuilder deleteBuilder = OrderDaoOpe.deleteBuilder();
            deleteBuilder.where().le("createTime", oneMouthAgo);
            deleteBuilder.delete();
            int num = deleteBuilder.delete();
            ZLog.e(TAG, "num" + num);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<RepHotReport> queryByNotMin(String time) {
        try {
            QueryBuilder<RepHotReport, Integer> queryBuilder = OrderDaoOpe.queryBuilder();
            queryBuilder.where().ne("createTime", time);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
