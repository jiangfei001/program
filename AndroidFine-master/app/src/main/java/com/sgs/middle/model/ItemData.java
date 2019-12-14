package com.sgs.middle.model;

import android.graphics.drawable.Drawable;

/**
 * <p>Created by 樊星(01211403) on 2018/2/27.<p>
 * 单条数据
 */
public class ItemData {
    /**
     * 数据分类
     */
    public static final int G_INDEX_APPCACHE = 0;
    public static final int G_INDEX_GARBAGE_FILE = 1;
    public static final int G_INDEX_APK = 2;
    public static final int G_INDEX_REMAIN = 3;
    public static final int G_INDEX_RAM = 4;
    /**
     * 数据类型
     */
    public static final int TYPE_GROUP = 0;
    public static final int TYPE_CHILD = 100;

    public int type = TYPE_GROUP;
    /**
     * 所属组索引
     */
    public int gIndex;
    public String title;
    public String subTitle;
    public Drawable icon;
    public long size;
    public boolean isChecked;
    /**
     * 携带的附件参数
     */
    public Object obj;

    public ItemData(int type, String title, int gIndex) {
        this.type = type;
        this.title = title;
        this.gIndex = gIndex;
    }

    public ItemData(int type, String title, int gIndex, boolean isChecked) {
        this.type = type;
        this.title = title;
        this.gIndex = gIndex;
        this.isChecked = isChecked;
    }
}
