package com.sgs;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.sgs.businessmodule.downloadModel.DownLoadService;
import com.sgs.businessmodule.websocketmodel.AppResponseDispatcher;
import com.sgs.businessmodule.websocketmodel.WebSocketClientManger;
import com.sgs.middle.receiver.CustomAlarmReceiver;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class AppContext extends Application {

    private static AppContext app;

    public static String TAG = "AppContext";
    public String getUserName = "jiangfei";

    public AppContext() {
    }

    public Activity nowActivity;


    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }

    private boolean isMainProcess;
    private ServiceConnection connection;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        registerUncaughtExceptionHandler();
        isMainProcess = isMainProcess();
        if (isMainProcess) {
            //initFileService();
            alarmUploadDataOnceDaily();
        }
    }


    /**
     * 初始化定位服务
     */
    private void initLocation() {
        // "ex"-GPS使用Android，网络定位使用百度, "ex2"-GPS使用Android，网络定位使用高德
        /*final MapLocationClient client = MapLocationClient.getInstance(this, "ex");
        client.addLocationListener((MapLocation location) -> {
            //将位置信息添加到点击流
            SfGather.sharedInstance().setLocation(location.getLatitude(), location.getLongitude());
            //上报设备信息
//                ReportHelper.report(ReportModel.deviceModel());
            try {
                List<HashMap<String, String>> gpsList = new ArrayList<>();
                HashMap<String, String> map = new HashMap<>();
                map.put("longitude", Double.toString(location.getLongitude()));
                map.put("latitude", Double.toString(location.getLatitude()));
                map.put("createTime", StringUtil.convertTimeStamp2DateTime(System.currentTimeMillis()));
                gpsList.add(map);
                //SfGatherUtil.trackGpsData(JSONObject.toJSONString(gpsList));
            } catch (Exception e) {
                //LogUtil.e(e);
            }
        });
        // 分配给项目组的key
        client.setKey("6ea9ab1baa0efb9e19094440c317e21b");

        MapLocationClientOption option = new MapLocationClientOption();

        // 网络请求超时时间
        option.setHttpTimeOut(30000);

        // 设置生产环境
        if (!BuildConfig.DEBUG_ABLE) {
            option.setIsProduct();
        }

        // 网络定位的时间间隔
        option.setInterval(isMobileUsedInHouse() ? 90 * 60 * 1000L : 10 * 60 * 1000L);

        // 运动状态GPS定位时间间隔
        option.setGPSMoveInterval(isMobileUsedInHouse() ? 30 * 60 * 1000L : 10 * 60 * 1000L);

        // 静止状态GPS定位时间间隔
        option.setGPStaticInterval(isMobileUsedInHouse() ? 90 * 60 * 1000L : 15 * 60 * 1000L);

        // 打包上传时间间隔
        option.setPackInterval(isMobileUsedInHouse() ? 100 * 60 * 1000L : 18 * 60 * 1000L);

        // 开启压缩打包
        option.setCompress(true);

        // 设置username
        option.getExtrasPara().putString("un", "appStore");

        client.setLocationOption(option);

        // AppStore作为系统App，系统完全授权
        // 但作为普通App，sdk需考虑权限问题(给地理信息部门提个bug，或者不用他们sdk，直接用百度或者高德)
        try {
            client.startLocation();
        } catch (Exception e) {
            LogUtil.e(e, "initLocation");
        }*/
    }

    private boolean isMainProcess() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            String mainProcess = packageInfo.applicationInfo.processName;

            int myPid = android.os.Process.myPid();
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.RunningAppProcessInfo myProcess = null;
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
            if (runningProcesses != null) {
                for (ActivityManager.RunningAppProcessInfo process : runningProcesses) {
                    if (process.pid == myPid) {
                        myProcess = process;
                        break;
                    }
                }
            }
            if (myProcess == null) {
                return true;
            }

            return myProcess.processName.equals(mainProcess);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "isMainProcess");
            return true;
        }
    }

    /**
     * 绑定常驻后台
     */
    public void initFileService() {
        Intent apkDownService = new Intent(this, DownLoadService.class);
        /* startService(apkDownService);*/
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(apkDownService);
        } else {
            this.startService(apkDownService);
        }*/
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // service connected
                Log.e(TAG, "onServiceConnected");
                //ProgramScheduledManager programScheduledManager = ProgramScheduledManager.getInstance();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // service disconnected
            }
        };
        bindService(apkDownService, connection, Context.BIND_ABOVE_CLIENT | Context.BIND_AUTO_CREATE);
    }

    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }


    public static void alarmUploadDataOnceDaily() {

        Calendar mCalendar = Calendar.getInstance();
        //得到日历实例，主要是为了下面的获取时间
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒 设置的为13点
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        //设置在几分提醒 设置的为25分
        mCalendar.set(Calendar.MINUTE, 0);
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        //上面设置的就是13点25分的时间点

        //获取上面设置的13点25分的毫秒值
        long selectTime = mCalendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(AppContext.getInstance(), CustomAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(), 0, intent, 0);
        //得到AlarmManager实例
        AlarmManager alarmManager = (AlarmManager) AppContext.getInstance().getSystemService(ALARM_SERVICE);

        //**********注意！！下面的两个根据实际需求任选其一即可*********

        /** * 单次提醒 * mCalendar.getTimeInMillis() 上面设置的13点25分的时间点毫秒值 */
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, mCalendar.getTimeInMillis(), pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, mCalendar.getTimeInMillis(), pi);
            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, mCalendar.getTimeInMillis(), pi);
            }
        } catch (Exception e) {
            Log.e("e", e.getMessage());
        }

        /** * 重复提醒 * 第一个参数是警报类型；下面有介绍 * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，但是我用的刷了MIUI的三星手机的实际效果是与单次提醒的参数一样，即设置的13点25分的时间点毫秒值 * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒 */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }

/*    public void initSchedule() {
        ProgramScheduledManager programScheduledManager = new ProgramScheduledManager.getInstance();
    }*/

    public Activity getNowActivity() {
        return nowActivity;
    }

    public void setNowActivity(Activity nowActivity) {
        this.nowActivity = nowActivity;
    }

}