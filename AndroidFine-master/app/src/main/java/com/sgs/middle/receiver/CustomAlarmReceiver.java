package com.sgs.middle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.zhangke.zlog.ZLog;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgs.AppContext;
import com.sgs.ReportUtil;
import com.sgs.businessmodule.taskModel.commandModel.command.CommandHelper;
import com.sgs.businessmodule.taskModel.taskList.CONTROLVOLUME;
import com.sgs.businessmodule.taskModel.taskList.SETOSTERMINAL;
import com.sgs.middle.utils.SharedPreferences;
import com.sgs.middle.utils.StringUtil;
import com.sgs.middle.utils.UsageStatsManagerUtil;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.ProgramUtil;

import java.text.SimpleDateFormat;
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
     * 定期执行AppHotArea上报
     */
    public static final String ACTION_SEND_APP_HOTAREA = "com.sf.appstore.business.receiver.custom.ACTION_SEND_APP_HOTAREA";
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
    public static final String ACTION_SEND_APP_CVDS = "com.sf.appstore.business.receiver.custom.ACTION_SEND_APP_CVDS";
    public static final int REQUEST_CODE_SEND_APP_CVDS = 3014;

    public static final String ACTION_SEND_APP_CLOSE = "com.sf.appstore.business.receiver.custom.ACTION_SEND_APP_CLOSE";
    public static final int REQUEST_CODE_SEND_APP_CLOSE = 3015;

    public static final String ACTION_SEND_APP_OPEN = "com.sf.appstore.business.receiver.custom.ACTION_SEND_APP_OPEN";
    public static final int REQUEST_CODE_SEND_APP_OPEN = 3016;

    @Override
    public void onReceive(Context context, Intent intent) {
        ZLog.e(TAG, "AppContext.getInstance().islogin:" + AppContext.getInstance().islogin);
        if (AppContext.getInstance().islogin) {
            ZLog.e(TAG, "CustomAlarmReceiver intent");
            if (intent == null) {
                ZLog.e(TAG, "CustomAlarmReceiver intent is null");
                return;
            }
            String action = intent.getAction();

            if (ACTION_PLAYGRAME_INIT.equals(action)) {

                long l = System.currentTimeMillis();
                //new日期对
                Date date = new Date(l);
                //转换提日期输出格式
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ZLog.e(TAG, "时间到,执行复原任务操作:" + dateFormat.format(date));
                ProgramScheduledManager.getInstance().initAllProgramTask();
                ReportUtil reportUtil = new ReportUtil();
                reportUtil.reportScence();
                UsageStatsManagerUtil.getInstance().alarmUploadDataOnceDaily();

                if (!StringUtil.isEmpty(SharedPreferences.getInstance().getString(CONTROLVOLUME.dingshi, ""))) {
                    cvds();
                }
                if (!StringUtil.isEmpty(SharedPreferences.getInstance().getString(SETOSTERMINAL.SETOSTERMINAL, ""))) {
                    setco();
                }
            } else if (ACTION_SEND_APP_HOTAREA.equals(action)) {
                UsageStatsManagerUtil.getInstance().alarmSendHotAreaReportUsage();
                ZLog.e(TAG, "时间到,执行复原任务操作:REPRORT");
                ReportUtil reportUtil = new ReportUtil();
                reportUtil.reportEvent();
            } else if (ACTION_SEND_APP_CVDS.equals(action)) {
                String vl = intent.getExtras().getString("vl");
                Toast.makeText(AppContext.getInstance(), "时间到，执行声音定时任务", Toast.LENGTH_LONG).show();
                ZLog.e(TAG, "时间到,执行定时声音:ACTION_SEND_APP_CVDS" + vl);
                CommandHelper.setStreamVolume(Integer.parseInt(vl), AppContext.getInstance());
            } else if (ACTION_SEND_APP_OPEN.equals(action)) {
                Toast.makeText(AppContext.getInstance(), "时间到，执行定时开机", Toast.LENGTH_LONG).show();
                ZLog.e(TAG, "时间到,执行定时开机:ACTION_SEND_APP_CVDS");
                CommandHelper.openOrClose(true);
            } else if (ACTION_SEND_APP_CLOSE.equals(action)) {
                ZLog.e(TAG, "时间到,执行关机:ACTION_SEND_APP_CVDS");
                CommandHelper.openOrClose(false);
            }
        }
    }

    public static void setco() {
        String prog = SharedPreferences.getInstance().getString(SETOSTERMINAL.SETOSTERMINAL, "");
        if (StringUtil.isEmpty(prog)) {
            return;
        }
        JSONObject j2 = JSON.parseObject(prog);
        String openTime = null;
        if (j2.containsKey("openTime")) {
            openTime = j2.getString("openTime");
        }
        String shuntDownTime = "";
        if (j2.containsKey("shuntDownTime")) {
            shuntDownTime = j2.getString("shuntDownTime");
        }
        List<String> list = null;
        if (j2.containsKey("weekList")) {
            String weekList = j2.getString("weekList");
            list = JSONObject.parseArray(weekList, String.class);
            ZLog.e("list", list.size() + "");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        }
        //计算时间进行定时
        Date date3 = new Date();
        String xinqi = ProgramUtil.getWeekOfDate(date3);
        if (j2.containsKey("weekList") && list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (xinqi.equals(list.get(i))) {
                    //定时音量
                    UsageStatsManagerUtil.alarmClose(shuntDownTime);
                    UsageStatsManagerUtil.alarmOpen(openTime);
                }
            }
        }
    }

    public static void cvds() {
        String prog = SharedPreferences.getInstance().getString(CONTROLVOLUME.dingshi, "");
        if (StringUtil.isEmpty(prog)) {
            return;
        }
        JSONObject j2 = JSON.parseObject(prog);
        String volumenum = null;
        if (j2.containsKey("volumenum")) {
            volumenum = j2.getString("volumenum");
        }
        String taskVolumeTime = "";
        if (j2.containsKey("taskVolumeTime")) {
            taskVolumeTime = j2.getString("taskVolumeTime");
        }
        List<String> list;
        if (j2.containsKey("weekList2")) {
            String weekListstr = j2.getString("weekList2");
            list = JSONObject.parseArray(weekListstr, String.class);
            ZLog.e("list", list.size() + "");
            Date date1 = new Date();
            String xinqi = ProgramUtil.getWeekOfDate(date1);
            for (int i = 0; i < list.size(); i++) {
                if (xinqi.equals(list.get(i))) {
                    //定时音量
                    UsageStatsManagerUtil.alarmcv(taskVolumeTime, volumenum);
                }
            }
        }
    }

    public static String TAG = CustomAlarmReceiver.class.getName();


}
