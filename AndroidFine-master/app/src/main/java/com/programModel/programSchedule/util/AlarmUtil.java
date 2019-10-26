package com.programModel.programSchedule.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

/**
 * Description
 * Created by langjian on 2017/3/19.
 * Version
 */

public class AlarmUtil {

    private static AlarmManager mAlarmManager;

    public static void setAlarm(Context context, long startduration, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
       mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10*1000), pendingIntent);
     // mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 10*1000, pendingIntent);

        // pendingIntent 为发送广播
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
        } else {
            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), startduration, pendingIntent);
        }
    }


    public static void cancelAlarm(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAlarmManager.cancel(pendingIntent);

    }
}
