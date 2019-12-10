package com.sgs;

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

import com.sgs.downloadModel.DownLoadService;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.websocketmodel.AppResponseDispatcher;
import com.sgs.websocketmodel.WebSocketClientManger;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;

import java.util.List;


public class AppContext extends Application {

    private static AppContext app;

    private String TAG = "AppContext";

    public AppContext() {
        app = this;
    }

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
        registerUncaughtExceptionHandler();
        //initSchedule();
        isMainProcess = isMainProcess();
        if (isMainProcess) {
            initFileService();
            initWebSocket();
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
    private void initFileService() {
        Intent apkDownService = new Intent(this, DownLoadService.class);
        startService(apkDownService);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // service connected
                ProgramScheduledManager programScheduledManager = ProgramScheduledManager.getInstance();
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

    private void initWebSocket() {
        WebSocketSetting setting = new WebSocketSetting();
        //连接地址，必填，例如 wss://echo.websocket.org
        //setting.setConnectUrl("ws://192.168.0.103:8081/multimedia/api/websocket/jf");//必填
        //setting.setConnectUrl("ws://192.168.0.97:8081/multimedia/api/websocket/jf");//必填
        setting.setConnectUrl("ws://192.168.0.106:8082/multimedia/api/websocket/jf");//必填
        //设置连接超时时间
        setting.setConnectTimeout(15 * 1000);

        //设置心跳间隔时间
        setting.setConnectionLostTimeout(45);

        //设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
        setting.setReconnectFrequency(60);

//        //设置Header
//        setting.setHttpHeaders(header);

        //设置消息分发器，接收到数据后先进入该类中处理，处理完再发送到下游
        setting.setResponseProcessDispatcher(new AppResponseDispatcher());
        //接收到数据后是否放入子线程处理，只有设置了 ResponseProcessDispatcher 才有意义
        setting.setProcessDataOnBackground(true);

        //网络状态发生变化后是否重连，
        //需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
        setting.setReconnectWithNetworkChanged(true);

        //通过 init 方法初始化默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.init(setting);
        //启动连接
        manager.start();

        //注意，需要在 AndroidManifest 中配置网络状态获取权限
        //注册网路连接状态变化广播
        WebSocketHandler.registerNetworkChangedReceiver(this);
        //初始化监听器
        WebSocketClientManger.getInstance();
    }

/*    public void initSchedule() {
        ProgramScheduledManager programScheduledManager = new ProgramScheduledManager.getInstance();
    }*/

}