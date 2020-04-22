package com.sgs.middle.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.sgs.AppContext;
import com.sgs.middle.receiver.CustomAlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Nick Song
 * @version 1.0
 * @Description UsageStatsManagerUtil
 * @date 2018/4/20
 */
public class UsageStatsManagerUtil {

    private static UsageStatsManager mUsmManager;
    private static UsageStatsManagerUtil mUsmManagerUtil;

    private static final String TYPE = "type";

    public static synchronized UsageStatsManagerUtil getInstance() {
        if (mUsmManagerUtil == null) {
            mUsmManagerUtil = new UsageStatsManagerUtil();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                mUsmManager = (UsageStatsManager) AppContext.getInstance().getSystemService(Context.USAGE_STATS_SERVICE);
            }
        }
        return mUsmManagerUtil;
    }

    private List<UsageStats> getUsageList() {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 30 * 60 * 1000L;
        List<UsageStats> usageList = new ArrayList<>();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && mUsmManager != null) {
            usageList = mUsmManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);
        }
        return usageList;
    }


    /**
     * 每隔10小时上报一次报表
     */
    public void alarmSendAppReportUsage() {
        AlarmManager alarmManager = (AlarmManager) AppContext.getInstance().getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(SystemClock.elapsedRealtime());
        // 1分钟上报一次打开次数
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);

        Intent it = new Intent(AppContext.getInstance(), CustomAlarmReceiver.class);
        it.setPackage(AppContext.getInstance().getPackageName());
        it.setAction(CustomAlarmReceiver.ACTION_SEND_APP_USAGE_COUNT);
        it.putExtra("time", System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(), CustomAlarmReceiver.REQUEST_CODE_SEND_APP_USAGE, it, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
        }
    }


    /**
     * 每天上报一次昨天的数据
     */
    public static void alarmUploadDataOnceDaily() {
        AlarmManager alarmManager = (AlarmManager) AppContext.getInstance().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(SystemClock.elapsedRealtime());
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 10);

        Intent it = new Intent(AppContext.getInstance(), CustomAlarmReceiver.class);
        it.setPackage(AppContext.getInstance().getPackageName());
        it.setAction(CustomAlarmReceiver.ACTION_UPLOAD_DATA_ONCE_DAILY);
        it.putExtra("time", System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(), CustomAlarmReceiver.REQUEST_CODE_UPLOAD_DATA_ONCE_DAILY, it, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
            }
        } catch (Exception e) {
            Log.e("", "");
        }
    }

    /* *//**
     * 每隔一定时间执行一次ping命令
     *//*
    public static void alarmExecutePing() {
        try {
            long pingInterval = (long) SharedPreferencesUtil.getInstance().get(ConstantUtil.PING_INTERVAL, 0L);
            if (pingInterval < ConstantUtil.NETWORK_MONITOR_MIN_INTERVAL) {
                return;
            }
            AlarmManager alarmManager = (AlarmManager) ContextUtil.getAppContext().getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(SystemClock.elapsedRealtime());
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + (int) (pingInterval / 60L));

            Intent it = new Intent(ContextUtil.getAppContext(), CustomAlarmReceiver.class);
            it.setPackage(ContextUtil.getAppContext().getPackageName());
            it.setAction(CustomAlarmReceiver.ACTION_EXECUTE_PING_AND_UPLOAD_RESULT);
            it.putExtra("time", System.currentTimeMillis());
            PendingIntent pi = PendingIntent.getBroadcast(ContextUtil.getAppContext(), CustomAlarmReceiver.REQUEST_CODE_PING_AND_UPLOAD_REQUEST, it, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }*/
}
