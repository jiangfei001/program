package com.sgs.middle.dbModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.middle.dbModel.entity.InstructionRequest;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "sqlite-test.db";
    private static final int DATABASE_VERSION = 1;

    private Map<String, Dao> daos = new HashMap();

    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, InstructionRequest.class);
            TableUtils.createTable(connectionSource, ProgarmPalyInstructionVo.class);
            TableUtils.createTable(connectionSource, MuTerminalMsg.class);
            TableUtils.createTable(connectionSource, ScenceReport.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ProgarmPalyInstructionVo.class, true);
            TableUtils.dropTable(connectionSource, InstructionRequest.class, true);
            TableUtils.dropTable(connectionSource, MuTerminalMsg.class, true);
            TableUtils.dropTable(connectionSource, ScenceReport.class, true);

            TableUtils.createTable(connectionSource, ProgarmPalyInstructionVo.class);
            TableUtils.createTable(connectionSource, InstructionRequest.class);
            TableUtils.createTable(connectionSource, MuTerminalMsg.class);
            TableUtils.createTable(connectionSource, ScenceReport.class);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
