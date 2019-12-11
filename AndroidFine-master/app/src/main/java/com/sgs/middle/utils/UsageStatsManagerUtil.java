package com.sgs.middle.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.sgs.AppContext;
import com.sgs.middle.receiver.CustomAlarmReceiver;

import java.util.Calendar;

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

    /**
     * 每天重新排期
     */
    public static void alarmOneDayStart() {
        AlarmManager alarmManager = (AlarmManager) AppContext.getInstance().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(SystemClock.elapsedRealtime());

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
            Log.e("e", "e");
        }
    }
}
