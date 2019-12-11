package com.sgs.middle.utils;

import android.util.Log;

public class FileUtil {
    public static String TAG = FileUtil.class.getName();

    public static String getFileNameByVirtualPath(String virtualPath) {
        String fName = virtualPath.trim();
        String fileName = fName.substring(fName.lastIndexOf("/") + 1);
        Log.e(TAG, "fileName:" + fileName);
        return fileName;
    }
}
