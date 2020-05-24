package com.sgs.middle.install;

import android.content.Context;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import com.zhangke.zlog.ZLog;


import java.io.File;
import java.lang.reflect.Method;

/**
 * 参考PackageManager
 * 安装卸载
 * https://github.com/paulononaka/Android-InstallInBackgroundSample
 * http://blog.csdn.net/sk719887916/article/details/50314017
 * 应用缓存清理
 * http://blog.csdn.net/wds1181977/article/details/51142565
 */
public class SFPkgManager {

    /**
     * 卸载监听
     */
    public interface OnDeletedPackaged {
        void packageDeleted(String packageName, int returnCode);
    }

    /**
     * 安装监听
     */
    public interface OnInstalledPackaged {
        void packageInstalled(String packageName, int returnCode);
    }

    /**
     * Flag parameter for {@link #installPackage} to indicate that you want to
     * replace an already installed package, if one exists.
     *
     * @hide
     */
    public static final int INSTALL_REPLACE_EXISTING = 0x00000002;
    /**
     * Flag parameter for {@link #installPackage} to indicate that it is okay
     * to install an update to an app where the newly installed app has a lower
     * version code than the currently installed app. This is permitted only if
     * the currently installed app is marked debuggable.
     *
     * @hide
     */
    public static final int INSTALL_ALLOW_DOWNGRADE = 0x00000080;
    /**
     * Flag parameter for {@link #installPackage} to indicate that all runtime
     * permissions should be granted to the package. If {@link #INSTALL_ALL_USERS}
     * is set the runtime permissions will be granted to all users, otherwise
     * only to the owner.
     *
     * @hide
     */
    public static final int INSTALL_GRANT_RUNTIME_PERMISSIONS = 0x00000100;
    /**
     * Flag parameter for {@link #installPackage} to indicate that this install
     * should immediately be visible to all users.
     *
     * @hide
     */
    public static final int INSTALL_ALL_USERS = 0x00000040;

    public static final int DELETE_KEEP_DATA = 0x00000001;
    public static final int DELETE_ALL_USERS = 0x00000002;

    /**
     * Return code for when package deletion succeeds. This is passed to the
     * {@link IPackageDeleteObserver} if the system succeeded in deleting the
     * package.
     *
     * @hide
     */
    public static final int DELETE_SUCCEEDED = 1;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * PackageManager.installPackage(android.net.Uri, IPackageInstallObserver, int) on success.
     *
     * @hide
     */
    public static final int INSTALL_SUCCEEDED = 1;

    private static Method METHOD_INSTALL = null;
    private static Method METHOD_DELETE = null;

    static {
        try {
            Class<?>[] types = new Class[]{Uri.class, IPackageInstallObserver.class, int.class, String.class};
            METHOD_INSTALL = PackageManager.class.getMethod("installPackage", types);

            Class<?>[] delTypes = new Class[]{String.class, IPackageDeleteObserver.class, int.class};
            METHOD_DELETE = PackageManager.class.getMethod("deletePackage", delTypes);
        } catch (Exception e) {
            ZLog.e("e", "Not support install silently");
        }
    }

    private IPackageInstallObserver installObserver;
    private IPackageDeleteObserver deleteObserver;

    private OnInstalledPackaged onInstalledPackaged;
    private OnDeletedPackaged onDeletePackaged;

    private PackageManager pm;

    public SFPkgManager(Context context) {
        pm = context.getPackageManager();

        installObserver = new IPackageInstallObserver.Stub() {

            @Override
            public void packageInstalled(String packageName, int returnCode) throws RemoteException {
                if (onInstalledPackaged != null) {
                    onInstalledPackaged.packageInstalled(packageName, returnCode);
                }
            }
        };
        deleteObserver = new IPackageDeleteObserver.Stub() {

            @Override
            public void packageDeleted(String packageName, int returnCode) throws RemoteException {
                if (onDeletePackaged != null) {
                    onDeletePackaged.packageDeleted(packageName, returnCode);
                }
            }
        };
    }

    /**
     * 设置卸载监听
     *
     * @param onDeletePackaged
     */
    public void setOnDeletePackaged(OnDeletedPackaged onDeletePackaged) {
        this.onDeletePackaged = onDeletePackaged;
    }

    /**
     * 设置安装监听
     *
     * @param onInstalledPackaged
     */
    public void setOnInstalledPackaged(OnInstalledPackaged onInstalledPackaged) {
        this.onInstalledPackaged = onInstalledPackaged;
    }

    /**
     * 安装apk
     *
     * @param apkFile
     * @throws Exception
     */
    public void installPackage(String apkFile) throws Exception {
        installPackage(new File(apkFile));
    }

    public void installPackage(File apkFile) throws Exception {
        if (!apkFile.exists()) {
            throw new IllegalArgumentException("File not exist");
        }
        Uri packageURI = Uri.fromFile(apkFile);
        installPackage(packageURI);
    }

    public void installPackage(Uri apkFile) throws Exception {
        METHOD_INSTALL.invoke(pm, apkFile, installObserver,
                INSTALL_REPLACE_EXISTING | INSTALL_ALLOW_DOWNGRADE | INSTALL_ALL_USERS,
                null);
    }

//    public void unInstallPackage(String packageName) throws InvocationTargetException, IllegalAccessException {
//        METHOD_UNINSTALL.invoke(pm, new Object[]{packageName, deleteObserver, DELETE_KEEP_DATA | DELETE_ALL_USERS});
//    }

    /**
     * 卸载应用
     *
     * @param packageName
     * @throws Exception
     */
    public void deletePackage(String packageName) throws Exception {
        METHOD_DELETE.invoke(pm, packageName, deleteObserver, DELETE_ALL_USERS);
    }
}
