package com.sgs.middle.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class InstallUtil {
    public static void installSilent(Context context, String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        File file = new File(filePath);
        if (file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
            return;
        }
        int installFlags = 0;
        Uri packageUri = Uri.fromFile(file);//file是要安装的apk文件
        String product = Build.BRAND;
        Log.e("pro", product);
        if (product.startsWith("rock")) {
            PackageManager pm = context.getPackageManager();
            silentInstall(pm, filePath);
        } else {
/**
 *android1.x-6.x
 *@param path 文件的路径
 */
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

    public static boolean silentInstall(PackageManager packageManager, String apkPath) {
        Class<?> pmClz = packageManager.getClass();
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Class<?> aClass = Class.forName("android.app.PackageInstallObserver");
                Constructor<?> constructor = aClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object installObserver = constructor.newInstance();
                Method method = pmClz.getDeclaredMethod("installPackage", Uri.class, aClass, int.class, String.class);
                method.setAccessible(true);
                method.invoke(packageManager, Uri.fromFile(new File(apkPath)), installObserver, 2, null);
            } else {
                Method method = pmClz.getDeclaredMethod("installPackage", Uri.class, Class.forName("android.content.pm.IPackageInstallObserver"), int.class, String.class);
                method.setAccessible(true);
                method.invoke(packageManager, Uri.fromFile(new File(apkPath)), null, 2, null);
            }
            return true;
        } catch (Exception e) {
            Log.e("ee", e.getMessage());
        }
        return false;
    }

 /*   private static class MyPackageInstallObserver implements IPackageInstallObserver {
        @Override
        public IBinder asBinder() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void packageInstalled(String arg0, int arg1)
                throws RemoteException {
            Log.e("ee", "arg0" + arg0 + "arg1" + arg1);
            // TODO Auto-generated method stub
        }
    }*/
}
