package com.sgs.middle.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.sgs.AppContext;
import com.sgs.middle.receiver.CustomAlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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

    public static void alarmUploadDataOnceDaily() {
        Log.e("alarmUpload", "alarmUploadDataOnceDaily");
        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();
        long firstTime = SystemClock.elapsedRealtime();//开机之后到现在的运行时间

        Calendar mCalendar = Calendar.getInstance();
        //得到日历实例，主要是为了下面的获取时间
        mCalendar = Calendar.getInstance();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒 设置的为0点
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        //设置在几分提醒 设置的为0分
        mCalendar.set(Calendar.MINUTE, (int) (Math.random() * 59));
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        long selectTime = mCalendar.getTimeInMillis();
        //选择的每天的定时时间即下班时间
        //如果当前时间大于设置的时间，那么从第二天的设定时间开始
        if (systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = mCalendar.getTimeInMillis();
        }

        //计算现在时间到设置时间的时间差
        long diffTime1 = selectTime - systemTime;
        firstTime += diffTime1;

        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(AppContext.getInstance(), CustomAlarmReceiver.class);
        intent.setAction(CustomAlarmReceiver.ACTION_PLAYGRAME_INIT);
        PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(), 0, intent, 0);
        //得到AlarmManager实例
        AlarmManager alarmManager = (AlarmManager) AppContext.getInstance().getSystemService(AppContext.getInstance().ALARM_SERVICE);

        //**********注意！！下面的两个根据实际需求任选其一即可*********

        /** * 单次提醒 * mCalendar.getTimeInMillis() 上面设置的13点25分的时间点毫秒值 */
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, firstTime, pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, firstTime, pi);
            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, firstTime, pi);
            }
        } catch (Exception e) {
            Log.e("e", e.getMessage());
        }

        /** * 重复提醒 * 第一个参数是警报类型；下面有介绍 * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，但是我用的刷了MIUI的三星手机的实际效果是与单次提醒的参数一样，即设置的13点25分的时间点毫秒值 * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒 */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }

    /**
     * 每隔10分钟上报一次报表
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
