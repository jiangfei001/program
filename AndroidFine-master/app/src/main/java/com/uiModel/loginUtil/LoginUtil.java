package com.uiModel.loginUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.EditText;

import com.sgs.AppContext;
import com.sgs.businessmodule.downloadModel.dbcontrol.FileHelper;
import com.sgs.middle.receiver.CustomAlarmReceiver;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.middle.utils.SharedPreferences;
import com.sgs.middle.utils.StringUtil;
import com.uiModel.activity.LoginActivity;
import com.zhangke.zlog.ZLog;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class LoginUtil {
    private static final String TAG = LoginUtil.class.getName();
    public static boolean isjihuo() {
        String terminalIdentity = SharedPreferences.getInstance().getString("terminalIdentity", "");
        String secretKey = SharedPreferences.getInstance().getString("secretKey", "");
        if (!TextUtils.isEmpty(terminalIdentity) && !TextUtils.isEmpty(secretKey)) {
            return true;
        } else {
            return false;
        }
    }

    public static void putTerminalIdentity(String terminalIdentity) {
        SharedPreferences.getInstance().putString("terminalIdentity", "");
    }

    public static void putIsZhuche(Boolean iszhuce) {
        SharedPreferences.getInstance().putBoolean("iszhuce", iszhuce);
    }


    public static boolean getIsZhuche() {
        return SharedPreferences.getInstance().getBoolean("iszhuce", false);
    }

    public static void putSecretKey(String secretKey) {
        SharedPreferences.getInstance().putString("secretKey", "");
    }

    public static void putTerminalIdenAndSecretKey(String terminalIdentity, String secretKey) {
        SharedPreferences.getInstance().putString("terminalIdentity", terminalIdentity);
        SharedPreferences.getInstance().putString("secretKey", secretKey);
    }

  /*  String terminalIdentity = SharedPreferences.getInstance().getString("terminalIdentity", "");
    public static String getSercetKey(Context context) {
        //1 从sp中拿
        android.content.SharedPreferences mContextSp = context.getSharedPreferences("terminalIdentity", Context.MODE_PRIVATE);
        String spUniqueID = mContextSp.getString(uniqueid1, "");
        if (!StringUtil.isEmpty(spUniqueID)) {
            ZLog.e(TAG, "找到了spUniqueID1:" + spUniqueID);
            FileHelper.putSDunique(spUniqueID, FileHelper.uniqueidf1);
            return spUniqueID;
        }
        //2 从sd卡中拿
        spUniqueID = FileHelper.getSDunique(FileHelper.uniqueidf1);
        if (!StringUtil.isEmpty(spUniqueID)) {
            ZLog.e(TAG, "找到了找到了fileUniqueID1:" + spUniqueID);
            //保存sp
            android.content.SharedPreferences.Editor editor = mContextSp.edit();
            editor.putString(uniqueid1, spUniqueID);
            editor.commit();
            return spUniqueID;
        }

        String id = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        ZLog.e(TAG, "androidId:" + androidId);
        if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
            try {
                UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                id = uuid.toString();
                ZLog.e(TAG, "id1:" + id);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getUUIDT();
            ZLog.e(TAG, "id2:" + id);
        }

        String uid = TextUtils.isEmpty(id) ? UUID.randomUUID().toString() : id;

        //保存sd卡
        ZLog.e(TAG, "保存spUniqueID:" + uid);
        FileHelper.putSDunique(uid, FileHelper.uniqueidf1);

        //保存sp
        android.content.SharedPreferences.Editor editor = mContextSp.edit();
        editor.putString(uniqueid1, uid);
        editor.commit();


        return uid;
    }*/

    public static String getTerminalIdentity() {
        String terminalIdentity = SharedPreferences.getInstance().getString("terminalIdentity", "");
        return terminalIdentity;
    }

    public static String getSecretKey() {
        String secretKey = SharedPreferences.getInstance().getString("secretKey", "");
        return secretKey;
    }


    /**
     * 每隔1分钟上报一次报表
     */
    public static void alarmSendReg() {
        AlarmManager alarmManager = (AlarmManager) AppContext.getInstance().getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(SystemClock.elapsedRealtime());
        // 10分钟上报一次打开次数
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 15);

        Intent it = new Intent(AppContext.getInstance(), CustomAlarmReceiver.class);
        it.setPackage(AppContext.getInstance().getPackageName());
        it.setAction(CustomAlarmReceiver.ACTION_SEND_APP_ZHUCE);
        it.putExtra("time", System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(), CustomAlarmReceiver.REQUEST_CODE_SEND_APP_ZHUCE, it, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pi);
        }
    }


    public static void getAMMap(EditText yonghuming, EditText shebeiName, HashMap hashMap, LoginActivity loginActivity) {
        hashMap.put("userName", yonghuming.getText().toString());

        hashMap.put("terminalIdentity", LoginUtil.getTerminalIdentity());

        hashMap.put("terminalName", !StringUtil.isEmpty(shebeiName.getText().toString()) ? shebeiName.getText().toString() : LoginUtil.getTerminalIdentity());
        //应用版本号
        hashMap.put("appVersion", DeviceUtil.getVersionName(loginActivity));
        //局域网IP地址
        hashMap.put("lanIp", DeviceUtil.getIPAddress(loginActivity));
        /*        //网关IP地址/
                hashMap.put("gatewayIp", DeviceUtil.getNetIp());*/
        //mac地址
        hashMap.put("mac", DeviceUtil.getWifiMacAddress(loginActivity));
        //分辨率
        hashMap.put("resolution", DeviceUtil.getDisplayMetricsPixels(loginActivity));
        //固件信息
        hashMap.put("firmwareInfo", DeviceUtil.getPhoneBrand() + "BuildLevel" + DeviceUtil.getBuildLevel());
        //CPU ID
        hashMap.put("cpuId", DeviceUtil.getCPU());
        //系统编号
        hashMap.put("systemNo", DeviceUtil.getBuildVersion());
        //设备身份编码
        hashMap.put("equipmentNo", DeviceUtil.getTerDeviceID(loginActivity));
        //设备序列号
        hashMap.put("equipmentSerial", DeviceUtil.getMobileSerial(loginActivity));
        //磁盘物理路径
        hashMap.put("physicalPath", DeviceUtil.getDir());
        //磁盘大小
        hashMap.put("diskSize", DeviceUtil.getDeviceTotalRam());
        //磁盘剩余大小
        hashMap.put("diskRest", DeviceUtil.getDeviceRemainRam());
        //最近连接时间
        hashMap.put("recentConnectTime", "");
        //地址
        hashMap.put("address", AppContext.getInstance().addr);

        hashMap.put("gatewayIp", DeviceUtil.getNetIp());
    }
}
