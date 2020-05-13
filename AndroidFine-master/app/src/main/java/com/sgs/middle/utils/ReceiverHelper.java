package com.sgs.middle.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sgs.middle.receiver.CustomAlarmReceiver;

/**
 * <p>Created by 樊星(01211403) on 2018/3/19.<p>
 * 通用广播注册帮助类
 */
public class ReceiverHelper {

    private ReceiverHelper() {
        // Constructor
    }

    /**
     * 注册app的安装与卸载结果广播监听
     *
     * @param context
     * @param appChangeReceiver
     */
    public static void registeAppChangeReceiver(Context context, BroadcastReceiver appChangeReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(CustomAlarmReceiver.ACTION_UNINSTALL_FAIL);
        filter.addAction(CustomAlarmReceiver.ACTION_INSTALL_FAIL);
        filter.addDataScheme("package");
        context.registerReceiver(appChangeReceiver, filter);
    }

    /**
     * 注册app安装与卸载全过程广播
     *
     * @param context
     * @param receiver
     */
    public static void registeAppFullChangeReceiver(Context context, BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_UNINSTALL_FAIL);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_UNINSTALLING);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_INSTALL_FAIL);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_INSTALLING);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_SEND_APP_HOTAREA);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_SEND_APP_CVDS);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_PLAYGRAME_INIT);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_SEND_APP_OPEN);
        intentFilter.addAction(CustomAlarmReceiver.ACTION_SEND_APP_CLOSE);
        intentFilter.addDataScheme("package");
        context.registerReceiver(receiver, intentFilter);
    }
}
