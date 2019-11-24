package com.commandModel.command;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.media.AudioManager;

import com.utils.DeviceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


/**
 * 设备信息的工具类
 * <p>
 * Created by Beluga_白鲸.
 */

public class CommandHelper {
    //关机
    public static void openOrClose() {
        try {
            //Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
            Runtime.getRuntime().exec(new String[]{"su", "-c", "shutdown"});
        } catch (IOException e) {
            e.printStackTrace();
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
    public static void setStreamVolume(int index, Context mContext) {
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

}


