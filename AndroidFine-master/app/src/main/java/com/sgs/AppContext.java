package com.sgs;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sgs.businessmodule.downloadModel.DownLoadService;
import com.sgs.businessmodule.taskModel.taskList.CONTROLVOLUME;
import com.sgs.businessmodule.taskModel.taskList.SETOSTERMINAL;
import com.sgs.businessmodule.websocketmodel.ActivityLifeManager;
import com.sgs.businessmodule.websocketmodel.CrashHandler;
import com.sgs.middle.receiver.CustomAlarmReceiver;
import com.sgs.middle.utils.SharedPreferences;
import com.sgs.middle.utils.StringUtil;
import com.sgs.middle.utils.UsageStatsManagerUtil;
import com.sgs.programModel.ProgramUtil;
import com.umeng.commonsdk.UMConfigure;

import java.util.Date;
import java.util.List;


public class AppContext extends Application {

    private static AppContext app;

    public static String TAG = "AppContext";
    public String userName = "123";
    public String addr = "";
    public String gonghao = "123456";
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
//BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            Log.e("add", addr);
            AppContext.getInstance().addr = addr;
        }
    }

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
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        // 初始化SDK
        UMConfigure.init(AppContext.getInstance(), "5e40dc73cb23d2b32c0001a2", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        CrashHandler.getInstance().initCrashHandlerException(this);

        //initFileService();
        if (isMainProcess) {
            UsageStatsManagerUtil.getInstance().alarmUploadDataOnceDaily();
            UsageStatsManagerUtil.getInstance().alarmSendHotAreaReportUsage();

            CustomAlarmReceiver.cvds();
            CustomAlarmReceiver.setco();

            ReportUtil reportUtil = new ReportUtil();
            reportUtil.reportEvent();
            reportUtil.reportScence();

            initLocation();
        }
        ActivityLifeManager.getInstance().

                setAppStatusListener(new ActivityLifeManager.AppStatusListener() {
                    @Override
                    public void goForeground() {
                    }

                    @Override
                    public void goBackgroud() {
                    }
                });
    }


    /**
     * 初始化定位服务
     */
    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(100000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
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





/*    public void initSchedule() {
        ProgramScheduledManager programScheduledManager = new ProgramScheduledManager.getInstance();
    }*/

    public Activity getNowActivity() {
        return nowActivity;
    }

    public void setNowActivity(Activity nowActivity) {
        this.nowActivity = nowActivity;
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            Log.e("ActivityLifeManager", "exit by application");
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(this.getPackageName());
            activityMgr.killBackgroundProcesses(this.getPackageName() + ":remote");
        } catch (Exception e) {
            Log.e("ActivityLifeManager", e.getMessage(), e);
        }

        System.exit(0);
    }
}