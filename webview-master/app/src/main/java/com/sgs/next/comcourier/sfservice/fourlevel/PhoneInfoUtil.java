package com.sgs.next.comcourier.sfservice.fourlevel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * 获取手机信息的工具类
 */
public class PhoneInfoUtil {
    public static final String IMEI1 = "IMEI1";
    public static final String IMEI2 = "IMEI2";
    public static final String MEID = "MEID";


    /**
     * 获取手机的IMEI和MEID的Map集合
     * 4.0的系统如果想获取MEID/IMEI1/IMEI2  其实是很难做到的。 因为你只能只用getDeviceId()  这个方法
     * 5.0的系统以上直接调用API getDeviceId(Int x) 由于当前项目的compileSdkVersion=22 所以只能通过反射
     *
     * @param context
     * @return
     */
    public static Map<String, String> getImeiAndMeid(Context context) {
        HashMap<String, String> map = new HashMap<>();
        if (Build.VERSION.SDK_INT >= 21) {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = null;
            try {
                method = manager.getClass().getMethod("getDeviceId", int.class);
                @SuppressLint("MissingPermission") String imei1 = manager.getDeviceId();
                String imei2 = (String) method.invoke(manager, 1);
                String meid = (String) method.invoke(manager, 2);
                map.put(IMEI1, imei1);
                map.put(IMEI2, imei2);
                map.put(MEID, meid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
            map.put(IMEI1, deviceId);
            map.put(IMEI2, deviceId);
            map.put(MEID, deviceId);
        }
        return map;
    }

    /**
     * 检测手机型号，是否是巴枪 ps:调用couriermain模块的方法（是否需要将公共类方法下沉？）
     * @return  true：是巴枪 false：其他
     */
    public static boolean validDevice() {
        boolean valid = false;
       /* try {
            Bundle bundle = ModularizationDelegate.getInstance().getData("com.sgs.unite:courierMain:checkValidDevice", null);
            valid = bundle.getBoolean("valid", false);

        } catch (Exception e) {
        }*/
        return valid;
    }
}
