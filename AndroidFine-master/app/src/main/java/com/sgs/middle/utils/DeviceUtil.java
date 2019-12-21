package com.sgs.middle.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sgs.AppContext;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by tiansj on 14/12/30.
 */
public class DeviceUtil {

    /**
     * 定义“没有读取手机状态的权限”的字符串
     */
    private static final String NO_PERMISSION_TO_READ_PHONE_STATE = "Could not get permission of android.permission.READ_PHONE_STATE";
    private static final String TAG = DateUtil.class.getName();

    /**
     * 获取系统总内存RAM
     *
     * @return
     */
    public static String getDeviceTotalRam() {
        ActivityManager activityManager = (ActivityManager) AppContext.getInstance().getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return StringUtil.convertRamWithUnit(memoryInfo.totalMem);
    }

    public static String getDir() {
        return AppContext.getInstance().getExternalCacheDir().getAbsolutePath();
    }

    /**
     * 获取巴枪设备序列号(设置--关于手机—状态信息—序列号)
     *
     * @return
     */
    public static String getMobileSerial() {
        return Build.SERIAL;
    }


    /**
     * 获取系统剩余RAM
     *
     * @return
     */
    public static String getDeviceRemainRam() {
        ActivityManager activityManager = (ActivityManager) AppContext.getInstance().getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return StringUtil.convertRamWithUnit(memoryInfo.availMem);
    }

    /**
     * 截取当前屏幕画面为bitmap图片
     *
     * @param activity
     * @param hasStatusBar 是否包含当前状态栏,true:包含
     * @return
     */
    public static Bitmap snapCurrentScreenShot(Activity activity, boolean hasStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        int deviceSize[] = getDeviceSize(activity);
        int coordinateY = 0;
        int cutHeight = deviceSize[1];
        if (!hasStatusBar) {
            Rect frame = new Rect();
            decorView.getWindowVisibleDisplayFrame(frame);
            coordinateY += frame.top;
            cutHeight -= frame.top;
        }
        Bitmap shot = Bitmap.createBitmap(bmp, 0, coordinateY, deviceSize[0], cutHeight);
        decorView.destroyDrawingCache();
        return shot;
    }

    /**
     * 获取屏幕大小[0]宽，[1]高
     *
     * @param context
     * @return
     */
    public static int[] getDeviceSize(Context context) {
        DisplayMetrics outMetrics = obtain(context);
        int[] sizes = new int[2];
        sizes[0] = outMetrics.widthPixels;
        sizes[1] = outMetrics.heightPixels;
        return sizes;
    }

    /**
     * 获取DisplayMetrics
     *
     * @param context
     * @return
     */
    private static DisplayMetrics obtain(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp,字体的转换
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取DisplayMetrics，包括屏幕高宽，密度等
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获得屏幕宽度 px
     *
     * @param context
     * @return
     */
    public static int getWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获得屏幕高度 px
     *
     * @param context
     * @return
     */
    public static int getHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取巴枪IMSI
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        try {
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (tm == null) {
                    return "";
                }
                return tm.getSubscriberId();
            } else {
                Log.e(TAG, NO_PERMISSION_TO_READ_PHONE_STATE);
            }
        } catch (Exception e) {
            Log.e("sdf", "Get IMSI error");
        }
        return "";
    }


    /**
     * 获取巴枪IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return "";
            }
            return tm.getDeviceId();
        } catch (Exception e) {
            Log.e(TAG, "Get IMEI error");
        }
        return "";
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean ret = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            ret = packageManager.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static String getDeviceId(Context context) {
        try {
            String strIMEI = getIMEI(context);
            if (strIMEI == null || strIMEI.equals("")) {
                strIMEI = getIMSI(context);
                if (strIMEI == null || strIMEI.equals("")) {
                    return "";
                }
            }
            String strTemp = strIMEI + strIMEI + strIMEI;
            String strMd5 = MiscUtil.getMD5(strTemp.getBytes());
            return strMd5;
        } catch (Exception exception1) {
        }
        return "";
    }

    /**
     * 获取设备宽度（px）
     *
     * @param context
     * @return
     */
    public static int deviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备高度（px）
     *
     * @param context
     * @return
     */
    public static int deviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * SD卡判断
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 是否有网
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 返回版本名字
     * 对应build.gradle中的versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId2(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有权限则返回""
            return "";
        } else {
            String deviceId = tm.getDeviceId();
            if (deviceId == null) {
                return "";
            } else {
                return deviceId;
            }
        }
    }

    /**
     * 获取巴枪Wifi mac地址
     *
     * @param context
     * @return
     */
    public static String getWifiMacAddress(Context context) {
        try {
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                String wifiMac;
                if (wm == null || wm.getConnectionInfo() == null) {
                    return getProperty(SF_WIFI_MAC, UNKNOWN);
                }
                wifiMac = wm.getConnectionInfo().getMacAddress();
                if (StringUtil.isEmpty(wifiMac) || ERROR_WIFI_MAC.equals(wifiMac)) {
                    wifiMac = getProperty(SF_WIFI_MAC, UNKNOWN);
                }
                return wifiMac;
            } else {
                Log.w("ACCESS_WIFI_STATE", "");
            }
        } catch (Exception e) {
            Log.w("ACCESS_WIFI_STATE", "");
        }
        return getProperty(SF_WIFI_MAC, UNKNOWN);
    }

    //获取手机的唯一标识
    @SuppressLint("MissingPermission")
    public static String getPhoneSign(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }
            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID();
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID());
        }
        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    private static String uuid;

    public static String getUUID() {
        SharedPreferences mShare = AppContext.getInstance().getSharedPreferences("uuid", Context.MODE_PRIVATE);
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString("uuid", uuid).commit();
        }
        return uuid;
    }

    public static void setUserName(String userName) {
        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = AppContext.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString("userName", userName);
        //步骤4：提交
        editor.commit();
    }

    public static void setConnectionTime() {
        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = AppContext.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString("ConnectionTime", new Date().toString());
        //步骤4：提交
        editor.commit();
    }

    public static String getConnectionTime() {
        SharedPreferences sharedPreferences = AppContext.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
        String connectionTime = sharedPreferences.getString("ConnectionTime", "");
        return connectionTime;
    }


    /**
     * 非正常WiFi Mac
     */
    private static final String ERROR_WIFI_MAC = "02:00:00:00:00:00";

    /**
     * 资产编号
     */
    private static final String SF_SCRIL_ID = "gsm.scril.sf";

    /**
     * WiFi Mac
     */
    private static final String SF_WIFI_MAC = "gsm.macaddress.sf";

    private static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, UNKNOWN));
        } catch (Exception e) {
            Log.w("ACCESS_WIFI_STATEe", "");
        }
        return value;
    }

    /**
     * unknown
     */
    private static final String UNKNOWN = "unknown";


    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取巴枪厂商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取内网ip地址
     * http://blog.csdn.net/u010248450/article/details/51470558
     * http://blog.csdn.net/shyboyes/article/details/74279137
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !(inetAddress instanceof Inet6Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, "Get ip address fail");
        }
        return null;
    }

    /**
     * 获取当前App进程的id
     *
     * @return
     */
    public static int getAppProcessId() {
        return android.os.Process.myPid();
    }

    /**
     * 获取当前App进程的Name
     *
     * @param context
     * @param processId
     * @return
     */
    public static String getAppProcessName(Context context, int processId) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有运行App的进程集合
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == processId) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                Log.e(DeviceUtil.class.getName(), e.getMessage(), e);
            }
        }
        return processName;
    }

    /**
     * 创建App文件夹
     *
     * @param appName
     * @param application
     * @return
     */
    public static String createAPPFolder(String appName, Application application) {
        return createAPPFolder(appName, application, null);
    }

    /**
     * 创建App文件夹
     *
     * @param appName
     * @param application
     * @param folderName
     * @return
     */
    public static String createAPPFolder(String appName, Application application, String folderName) {
        File root = Environment.getExternalStorageDirectory();
        File folder;
        /**
         * 如果存在SD卡
         */
        if (isSDCardAvailable() && root != null) {
            folder = new File(root, appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } else {
            /**
             * 不存在SD卡，就放到缓存文件夹内
             */
            root = application.getCacheDir();
            folder = new File(root, appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        if (folderName != null) {
            folder = new File(folder, folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        return folder.getAbsolutePath();
    }

    /**
     * 通过Uri找到File
     *
     * @param context
     * @param uri
     * @return
     */
    public static File uri2File(Activity context, Uri uri) {
        File file;
        String[] project = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = context.getContentResolver().query(uri, project, null, null, null);
        if (actualImageCursor != null) {
            int actual_image_column_index = actualImageCursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualImageCursor.moveToFirst();
            String img_path = actualImageCursor
                    .getString(actual_image_column_index);
            file = new File(img_path);
        } else {
            file = new File(uri.getPath());
        }
        if (actualImageCursor != null) actualImageCursor.close();
        return file;
    }

    public static String getCPU() {
        return android.os.Build.CPU_ABI;
    }

    /**
     * 获取AndroidManifest.xml里 的值
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetaData(Context context, String name) {
        String value = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取ip（内网ip）
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }


            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }


    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取IP(外网ip、公网ip)
     */
    public static String getNetIp() {
        String IP = "";
        try {
            String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
            URL url = new URL(address);

            //URLConnection htpurl=url.openConnection();

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36"); //设置浏览器ua 保证不出现503

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();

                // 将流转化为字符串
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));

                String tmpString = "";
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append(tmpString + "\n");
                }

                JSONObject jsonObject = new JSONObject(retJSON.toString());
                String code = jsonObject.getString("code");

                if (code.equals("0")) {
                    JSONObject data = jsonObject.getJSONObject("data");

                    //格式：180.000.00.000(中国区上海上海电信)
//                    IP = data.getString("ip") + "(" + data.getString("country")
//                            + data.getString("area") + "区"
//                            + data.getString("region") + data.getString("city")
//                            + data.getString("isp") + ")";

                    //格式：180.000.00.000
                    IP = data.getString("ip");

                    Log.e("提示", "您的IP地址是：" + IP);
                } else {
                    IP = "";
                    Log.e("提示", "IP接口异常，无法获取IP地址！");
                }
            } else {
                IP = "";
                Log.e("提示", "网络连接异常，无法获取IP地址！");
            }
        } catch (Exception e) {
            IP = "";
            Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
        }
        return IP;

    }

    public static String streamreader(InputStream is) throws IOException {
        //转换为字符
        InputStreamReader isr = new InputStreamReader(is, "gbk");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder stringBuilder = new StringBuilder();
        for (String tmp = br.readLine(); tmp != null; tmp = br.readLine()) {
            stringBuilder.append(tmp);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics;
    }

    /**
     * 获取巴枪屏幕分辨率
     *
     * @param context
     * @return 如:768*1096
     */
    public static String getDisplayMetricsPixels(Context context) {
        try {
            DisplayMetrics dm = getDisplayMetrics(context);
            return dm.widthPixels + "*" + dm.heightPixels;
        } catch (Exception e) {
            Log.e(TAG, "GetDisplayMetricsPixels error");
        }
        return "";
    }

    /**
     * 获取巴枪型号
     *
     * @return 巴枪类型 HHT7AX、HHT7A、HHT7BX、HHT7B、HHT7AG、HHT7BG
     */
    public static String getMobileModel() {
        return Build.MODEL;
    }
}
