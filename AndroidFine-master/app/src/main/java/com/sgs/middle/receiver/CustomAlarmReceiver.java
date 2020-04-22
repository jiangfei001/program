package com.sgs.middle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.RedHotReportRequestManager;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.ScenceReportRequestManager;
import com.sgs.businessmodule.upReportModel.RepHotReport;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.middle.utils.UsageStatsManagerUtil;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.SendToServerUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomAlarmReceiver extends BroadcastReceiver {
    /**
     * 安装卸载失败广播
     */
    public static final String ACTION_INSTALL_FAIL = "com.sf.appstore.business.receiver.custom.ACTION_INSTALL_FAIL";
    public static final String ACTION_INSTALLING = "com.sf.appstore.business.receiver.custom.ACTION_INSTALLING";
    public static final String ACTION_UNINSTALL_FAIL = "com.sf.appstore.business.receiver.custom.ACTION_UNINSTALL_FAIL";
    public static final String ACTION_UNINSTALLING = "com.sf.appstore.business.receiver.custom.ACTION_UNINSTALLING";
    /**
     * 定期执行app安装策略
     */
    public static final String ACTION_COMMAND_APP = "com.sf.appstore.business.receiver.custom.ACTION_COMMAND_APP";
    public static final int REQUEST_CODE_COMMAND_APP = 600;
    /**
     * 定期执行自身版本检测
     */
    public static final String ACTION_CHECK_UPDATE_SERVICE = "com.sf.appstore.business.receiver.custom.ACTION_CHECK_UPDATE_SERVICE";
    public static final int REQUEST_CODE_CHECK_UPDATE = 0;
    /**
     * 定期执行App打开次数的数据上报
     */
    public static final String ACTION_SEND_APP_USAGE_COUNT = "com.sf.appstore.business.receiver.custom.ACTION_SEND_APP_USAGE_COUNT";
    public static final int REQUEST_CODE_SEND_APP_USAGE = 3000;

    /**
     * 每天上报一次数据：电量、流量、设备信息
     */
    public static final String ACTION_UPLOAD_DATA_ONCE_DAILY = "com.sf.appstore.business.receiver.custom.ACTION_UPLOAD_DATA_ONCE_DAILY";
    public static final int REQUEST_CODE_UPLOAD_DATA_ONCE_DAILY = 3001;

    /**
     * 每天上报两次数据：跌落、已安装App列表
     */
    public static final String ACTION_UPLOAD_DATA_TWICE_DAILY = "com.sf.appstore.business.receiver.custom.ACTION_UPLOAD_DATA_TWICE_DAILY";
    public static final int REQUEST_CODE_UPLOAD_DATA_TWICE_DAILY = 3003;

    /**
     * 一周检测一次有无新的ROM
     */
    public static final String ACTION_GET_NEW_ROM = "com.sf.appstore.business.receiver.custom.ACTION_GET_NEW_ROM";
    public static final int REQUEST_CODE_GET_NEW_ROM = 3004;

    /**
     * 每天上报三次数据：App消耗的RAM
     */
    public static final String ACTION_UPLOAD_DATA_THREE_TIMES_DAILY = "com.sf.appstore.business.receiver.custom.ACTION_UPLOAD_DATA_THREE_TIMES_DAILY";
    public static final int REQUEST_CODE_UPLOAD_DATA_THREE_TIMES_DAILY = 3005;

    /**
     * 定期执行ping命令并上报执行结果
     */
    public static final String ACTION_EXECUTE_PING_AND_UPLOAD_RESULT = "com.sf.appstore.business.receiver.custom.ACTION_PING_AND_UPLOAD_RESULT";
    public static final int REQUEST_CODE_PING_AND_UPLOAD_REQUEST = 3007;

    /**
     * 定期执行nslookup命令并上报执行结果
     */
    public static final String ACTION_EXECUTE_NSLOOKUP_AND_UPLOAD_RESULT = "com.sf.appstore.business.receiver.custom.ACTION_NSLOOKUP_AND_UPLOAD_RESULT";
    public static final int REQUEST_CODE_NSLOOKUP_AND_UPLOAD_REQUEST = 3008;

    /**
     * 定期执行telnet命令并上报执行结果
     */
    public static final String ACTION_EXECUTE_TELNET_AND_UPLOAD_RESULT = "com.sf.appstore.business.receiver.custom.ACTION_TELNET_AND_UPLOAD_RESULT";
    public static final int REQUEST_CODE_TELNET_AND_UPLOAD_REQUEST = 3009;

    /**
     * 定期执行dig命令并上报执行结果
     */
    public static final String ACTION_EXECUTE_DIG_AND_UPLOAD_RESULT = "com.sf.appstore.business.receiver.custom.ACTION_DIG_AND_UPLOAD_RESULT";
    public static final int REQUEST_CODE_DIG_AND_UPLOAD_REQUEST = 3010;

    /**
     * 定期上报巴枪设备硬件信息数据
     */
    public static final String ACTION_UPLOAD_DEVICE_HARDWARE_INFO_RESULT = "com.sf.appstore.business.receiver.custom.ACTION_UPLOAD_DEVICE_HARDWARE_INFO_RESULT";
    public static final int REQUEST_CODE_UPLOAD_DEVICE_HARDWARE_INFO_REQUEST = 3012;

    /**
     * 每天重新刷新一次
     */
    public static final String ACTION_PLAYGRAME_INIT = "com.sf.appstore.business.receiver.custom.ACTION_UPLOAD_DATA_ONCE_DAILY";
    public static final int REQUEST_CODE_PLAYGRAME_INIT = 3013;

    public static final String REPRORT = "com.sf.appstore.business.receiver.custom.ACTION_REPORT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.e(TAG, "CustomAlarmReceiver intent is null");
            return;
        }
        String action = intent.getAction();

        if (ACTION_PLAYGRAME_INIT.equals(action)) {
            long l = System.currentTimeMillis();
            //new日期对
            Date date = new Date(l);
            //转换提日期输出格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.e(TAG, "时间到,执行复原任务操作:" + dateFormat.format(date));
            ProgramScheduledManager.getInstance().initAllProgramTask();
            UsageStatsManagerUtil.getInstance().alarmUploadDataOnceDaily();
        }

        if (ACTION_SEND_APP_USAGE_COUNT.equals(action)) {
            UsageStatsManagerUtil.getInstance().alarmSendAppReportUsage();
            Log.e(TAG, "时间到,执行复原任务操作:REPRORT");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取除了今天的记录
                    Calendar cal = Calendar.getInstance();
                    String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                    Log.e(TAG, "scenceReports:" + today);
                    List<ScenceReport> scenceReports = ScenceReportRequestManager.getInstance().queryByNotToday(today);
                    if (scenceReports != null) {
                        Log.e(TAG, "scenceReports:" + scenceReports);
                        SendToServerUtil.sendScenctToServer(scenceReports);
                        ScenceReportRequestManager.getInstance().delByNotToday(today);
                    } else {
                        Log.e(TAG, "scenceReports:" + null);
                    }

                    List<RepHotReport> repHotReports = RedHotReportRequestManager.getInstance().queryByNotToday(today);
                    if (repHotReports != null) {
                        Log.e(TAG, "repHotReports:" + repHotReports);
                        SendToServerUtil.sendRepHotareaToServer(repHotReports);
                        RedHotReportRequestManager.getInstance().delByNotToday(today);
                    } else {
                        Log.e(TAG, "repHotReports:" + null);
                    }

                }
            }).start();
        }
    }

    public static String TAG = CustomAlarmReceiver.class.getName();


}
