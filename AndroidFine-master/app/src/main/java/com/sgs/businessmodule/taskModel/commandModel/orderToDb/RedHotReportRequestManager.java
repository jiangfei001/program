package com.sgs.businessmodule.taskModel.commandModel.orderToDb;

import android.content.Context;

import com.sgs.AppContext;
import com.sgs.businessmodule.upReportModel.RepHotReport;
import com.sgs.middle.dbModel.dao.RedHotReportDao;

import java.util.List;

public class RedHotReportRequestManager {
    private final String TAG = "DataManager";

    private Context context;

    private RedHotReportRequestManager(Context context) {
        this.context = context;
    }

    private static RedHotReportRequestManager instance;

    public static RedHotReportRequestManager getInstance() {
        if (instance == null) {
            synchronized (RedHotReportRequestManager.class) {
                if (instance == null) {
                    instance = new RedHotReportRequestManager(AppContext.getInstance());
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
    public void saveInstructionRequest(RepHotReport scenceReport) {
        new RedHotReportDao(context).add(scenceReport);
    }

    public List<RepHotReport> queryByDate(String reportDate) {
        return new RedHotReportDao(context).queryByDate(reportDate);
    }

    public List<RepHotReport> queryByNotMin(String reportDate) {
        return new RedHotReportDao(context).queryByNotMin(reportDate);
    }

    public RepHotReport queryByDateAndScenceId(int scenceId, String reportDate, String areaName, String pageName) {
        return new RedHotReportDao(context).queryByDateAndScenceId(scenceId, reportDate, areaName, pageName);
    }

    public void delByDate(String yesterday) {
        new RedHotReportDao(context).delByDate(yesterday);
    }

    public void delByNotToday(String today) {
        new RedHotReportDao(context).delByDate(today);
    }

    public void delOneMouthAgo(String onMouthAgo) {
        new RedHotReportDao(context).delOneMouthAgo(onMouthAgo);
    }
}
