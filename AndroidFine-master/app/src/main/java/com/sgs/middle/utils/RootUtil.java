package com.sgs.middle.utils;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;

public class RootUtil {

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath(); RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */

    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }

    /**
     * 手机截图
     *
     * @return 返回截图的路径
     */
    public static String getScreenshot() {
        Process process = null;
        String filePath = "mnt/sdcard/" + System.currentTimeMillis() + ".png";
        try {
            process = Runtime.getRuntime().exec("su");
            PrintStream outputStream = null;
            outputStream = new PrintStream(new BufferedOutputStream(process.getOutputStream(), 8192));
            outputStream.println("screencap -p " + filePath);
            outputStream.flush();
            outputStream.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return filePath;
    }
}
