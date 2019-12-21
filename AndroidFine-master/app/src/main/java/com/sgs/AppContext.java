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

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sgs.businessmodule.downloadModel.DownLoadService;
import com.sgs.middle.receiver.CustomAlarmReceiver;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class AppContext extends Application {

    private static AppContext app;

    public static String TAG = "AppContext";
    public String getUserName = "jiangfei";
    public String addr = "";

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

        //initFileService();
        if (isMainProcess) {
            alarmUploadDataOnceDaily();
            initLocation();
        }

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

        option.setScanSpan(10000);
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