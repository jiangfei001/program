package com.sgs.businessmodule.taskModel.commandModel.command;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.zhangke.zlog.ZLog;
import android.widget.Toast;

import com.sgs.AppContext;
import com.sgs.middle.utils.DeviceUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHelper {
    //关机
    public static void openOrClose(boolean b) {
        if (b) {
            Handler handler = new Handler(Looper.getMainLooper()) {
            };
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AppContext.getInstance(), "执行开机", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Handler handler = new Handler(Looper.getMainLooper()) {
            };
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AppContext.getInstance(), "执行关机", Toast.LENGTH_SHORT).show();
                }
            });
            try {
                //Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                Runtime.getRuntime().exec(new String[]{"su", "-c", "shutdown"});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Runtime.getRuntime().exec(new String[]{"su","-c","shutdown"})
    }

    //重启
    public static void reboot(Context context) {
        try {
            Runtime.getRuntime().exec("su -c reboot");
           /* PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            pm.reboot("");*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Runtime.getRuntime().exec(new String[]{"su","-c","shutdown"})
    }

    //index 传入设置音量的值
    public static void setStreamVolume(final int index, Context mContext) {
        Handler handler = new Handler(Looper.getMainLooper()) {
        };
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AppContext.getInstance(), "设置音量设置" + index, Toast.LENGTH_LONG).show();
            }
        });
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取设备最大音量
        int volm = index * streamMaxVolume / 100;
        //第一个参数为设置音量的类型（通话，铃声，音乐等）
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
            /*修改第二个参数为一个固定的值，就是设置成功。
            下面的方法：获取音乐类型的音频流的最大值*/
                volm, AudioManager.FLAG_PLAY_SOUND);
    }

    //获取硬件信息
//重启
    public static void getDevice(Context context) {
        String devicester = DeviceUtil.getDeviceId(context);
    }
    //删除本地文件

    /**
     * 删除文件夹
     *
     * @param path
     */
    public static void deleteDir(String path) {
        File dir = new File(path);
        deleteDirWihtFile(dir);
    }

    //删除文件及文件夹
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
//固件升级

//app升级

    /**
     * 卸载应用入口提示
     *
     * @param
     */
    public static void startUninstall(String pkg, Context context) {
        //判断此应用程序是否存在
        Boolean pkgExist;
        pkgExist = appExist(context, pkg);
        if (!pkgExist) {
        } else {
            //静默卸载
            Boolean uninstallSuccess = uninstall(pkg);
            if (uninstallSuccess) {
            } else {
            }
        }

    }


/**
 * 静默安装的实现类，调用install()方法执行具体的静默安装逻辑。
 * 原文地址：http://blog.csdn.net/guolin_blog/article/details/47803149
 * @author guolin
 * @since 2015/12/7
 */
    /**
     * 执行具体的静默安装逻辑，需要手机ROOT。
     *
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public static boolean install(String apkPath) {
        String product = Build.PRODUCT;
        if (false) {
            return true;
        } else {
            boolean result = false;
            DataOutputStream dataOutputStream = null;
            BufferedReader errorStream = null;
            try {
                // 申请su权限
                Process process = Runtime.getRuntime().exec("su");
                dataOutputStream = new DataOutputStream(process.getOutputStream());
                // 执行pm install命令
                String command = "pm install -r " + apkPath + "\n";
                dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
                dataOutputStream.flush();
                dataOutputStream.writeBytes("exit\n");
                dataOutputStream.flush();
                process.waitFor();
                errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String msg = "";
                String line;
                // 读取命令的执行结果
                while ((line = errorStream.readLine()) != null) {
                    msg += line;
                }
                ZLog.d("TAG", "install msg is " + msg);
                // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
                if (!msg.contains("Failure")) {
                    result = true;
                }
            } catch (Exception e) {
                ZLog.e("TAG", e.getMessage(), e);
            } finally {
                try {
                    if (dataOutputStream != null) {
                        dataOutputStream.close();
                    }
                    if (errorStream != null) {
                        errorStream.close();
                    }
                } catch (IOException e) {
                    ZLog.e("TAG", e.getMessage(), e);
                }
            }
            return result;
        }
    }


    /**
     * 静默卸载App
     *
     * @param packageName 包名
     * @return 是否卸载成功
     */
    private static boolean uninstall(String packageName) {
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        try {
            process = new ProcessBuilder("pm", "uninstall", packageName).start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            // KLog.d("e = " + e.toString());
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                //KLog.d("Exception : " + e.toString());
            }
            if (process != null) {
                process.destroy();
            }
        }
        //如果含有"success"单词则认为卸载成功
        return successMsg.toString().equalsIgnoreCase("success");
    }

    /**
     * 判断应用是否存在
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 是否存在
     */
    private static boolean appExist(Context context, String packageName) {
        try {
            List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(0);
            for (PackageInfo packageInfo : packageInfoList) {
                if (packageInfo.packageName.equalsIgnoreCase(packageName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // KLog.d(e.toString());
        }
        return false;
    }
// 截屏

    /**
     * 截取当前屏幕画面为bitmap图片
     *
     * @param activity
     * @param hasStatusBar 是否包含当前状态栏,true:包含
     * @return
     */
    public static void snapCurrentScreenShot(Activity activity, boolean hasStatusBar) {
        DeviceUtil.snapCurrentScreenShot(activity, hasStatusBar);
    }
//重启终端


    /**
     * type=11表示巴枪上报硬件相关数据：如,电池最大容量、系统总内存、系统剩余内存
     */
    public static String trackHardwareInfo() {
        Map<String, String> properties = new HashMap<>();
        properties.put("totalRam", DeviceUtil.getDeviceTotalRam());
        properties.put("remainRam", DeviceUtil.getDeviceRemainRam());
        properties.put("imei", DeviceUtil.getIMEI(AppContext.getInstance()));
        properties.put("DisplayMetricsPixels", DeviceUtil.getDisplayMetricsPixels(AppContext.getInstance()));
        properties.put("BuildVersion", DeviceUtil.getBuildVersion());
        properties.put("Manufacturer", DeviceUtil.getManufacturer());
        properties.put("LocalIpAddress", DeviceUtil.getLocalIpAddress());
        String hainfo = properties.toString();
        return hainfo;
    }
}


