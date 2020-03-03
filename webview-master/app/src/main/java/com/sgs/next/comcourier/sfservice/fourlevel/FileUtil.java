package com.sgs.next.comcourier.sfservice.fourlevel;

import android.os.Environment;

import java.io.File;

/**
 * author: 01370340
 * data: 2019/1/11
 * description:存放和文件相关的工具方法
 */
public class FileUtil {
    /**
     * 默认services DB的存储路径 使用加密
     *
     * @return
     */
    public static String getInnerDbPath() {
        String packageName = "com.example.app";

        String DB_PATH = "/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + packageName;
        return DB_PATH;
    }

    /**
     * 判断某个文件是否存在于sd卡内
     *
     * @param fileName
     * @return
     */
    public static File getFileInExternalStoragePath(String fileName) {
        String extSDCardPath = "";
        extSDCardPath = getInnerDbPath();
        File fileInSdCard = new File(extSDCardPath, fileName);
        //if (!externalStorageDirectory.exists()) {
        //    return fileInSdCard;
        //}
        //        File[] files = externalStorageDirectory.getParentFile().listFiles();
        //long dbLastModifiedTime = 0;

        //        if (files!=null && files.length>0){
        //            for (File sdCard : files) {
        //if (externalStorageDirectory.canWrite() && externalStorageDirectory.isDirectory()) {
        //    File file = new File(externalStorageDirectory, fileName);
        //    long lastModifiedTime = file.lastModified();
        //    if (file.exists() && lastModifiedTime > dbLastModifiedTime) {
        //        fileInSdCard = file;
        //        dbLastModifiedTime = lastModifiedTime;
        //    }
        //}
        //            }
        //        }

        return fileInSdCard;
    }


}
