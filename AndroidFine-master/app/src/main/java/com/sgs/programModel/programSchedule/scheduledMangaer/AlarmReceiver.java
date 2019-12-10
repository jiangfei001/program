package com.sgs.programModel.programSchedule.scheduledMangaer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Description
 * Created by langjian on 2017/3/19.
 * Version
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "闹钟提示：时间到！", Toast.LENGTH_LONG).show();
    }
}
