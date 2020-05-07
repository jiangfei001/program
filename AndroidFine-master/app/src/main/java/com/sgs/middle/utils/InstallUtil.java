package com.sgs.middle.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
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

    /*
        public void install() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startInstallO();
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) startInstallN();
            else startInstall();
        }

        /**
         * android1.x-6.x
         */
     /*private void startInstall() {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.parse("file://" + mPath), "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mAct.startActivity(install);
    }

    //android7.x

    private void startInstallN() {
        //参数1 上下文, 参数2 在AndroidManifest中的android:authorities值, 参数3  共享的文件
        Uri apkUri = FileProvider.getUriForFile(mAct, SyncStateContract.Constants.AUTHORITY, new File(mPath));
        Intent install = new Intent(Intent.ACTION_VIEW);
        //由于没有在Activity环境下启动Activity,设置下面的标签
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        mAct.startActivity(install);
    }

    *//**
            *android8.x
     *//*

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallO() {
        boolean isGranted = mAct.getPackageManager().canRequestPackageInstalls();
        if (isGranted) startInstallN();//安装应用的逻辑(写自己的就可以)
        else new AlertDialog.Builder(mAct)
                .setCancelable(false)
                .setTitle("安装应用需要打开未知来源权限，请去设置中开启权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int w) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        mAct.startActivityForResult(intent, UNKNOWN_CODE);
                    }
                })
                .show();
    }*/

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
