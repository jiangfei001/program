package com.sgs.middle.model;

import android.graphics.drawable.Drawable;

/**
 * @Description CacheListItem
 * @author Nick Song
 * @date 2018/1/16
 * @version 1.0
 */
public class CacheListItem {

    private long mCacheSize;
    private String mPackageName, mApplicationName;
    private Drawable mIcon;

    public CacheListItem(String packageName, String applicationName, Drawable icon, long cacheSize) {
        mCacheSize = cacheSize;
        mPackageName = packageName;
        mApplicationName = applicationName;
        mIcon = icon;
    }

    public Drawable getApplicationIcon() {
        return mIcon;
    }

    public String getApplicationName() {
        return mApplicationName;
    }

    public long getCacheSize() {
        return mCacheSize;
    }

    public String getPackageName() {
        return mPackageName;
    }
}
