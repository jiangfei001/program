package com.sgs.middle.utils;

import com.zhangke.zlog.ZLog;

public class FileUtil {
    public static String TAG = FileUtil.class.getName();

    public static String getFileNameByVirtualPath(String virtualPath) {
        String fName = virtualPath.trim();
        String fileName = fName.substring(fName.lastIndexOf("/") + 1);
        ZLog.e(TAG, "fileName:" + fileName);
        return fileName;
    }
}
