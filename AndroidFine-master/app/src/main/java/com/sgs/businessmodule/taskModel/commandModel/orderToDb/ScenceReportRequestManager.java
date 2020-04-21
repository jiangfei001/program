package com.sgs.businessmodule.taskModel.commandModel.orderToDb;

import android.content.Context;

import com.sgs.AppContext;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.middle.dbModel.dao.ScenceReportDao;

import java.util.List;

public class ScenceReportRequestManager {
    private final String TAG = "DataManager";

    private Context context;

    private ScenceReportRequestManager(Context context) {
        this.context = context;
    }

    private static ScenceReportRequestManager instance;

    public static ScenceReportRequestManager getInstance() {
        if (instance == null) {
            synchronized (ScenceReportRequestManager.class) {
                if (instance == null) {
                    instance = new ScenceReportRequestManager(AppContext.getInstance());
                }
            }
        }
        return instance;
    }

    /**
     * 保存基础任务
     *
     * @param scenceReport instructionRequest == null >> instructionRequest = new User();
     */
    public void saveInstructionRequest(ScenceReport scenceReport) {
        new ScenceReportDao(context).add(scenceReport);
    }

    public List<ScenceReport> queryByDate(String reportDate) {
        return new ScenceReportDao(context).queryByDate(reportDate);
    }

    public List<ScenceReport> queryByNotToday(String reportDate) {
        return new ScenceReportDao(context).queryByNotToday(reportDate);
    }

    public ScenceReport queryByDateAndScenceId(int scenceId, String reportDate) {
        return new ScenceReportDao(context).queryByDateAndScenceId(scenceId, reportDate);
    }

    public void delByDate(String yesterday) {
        new ScenceReportDao(context).delByDate(yesterday);
    }
    public void delByNotToday(String yesterday) {
        new ScenceReportDao(context).delByNotToday(yesterday);
    }


}
