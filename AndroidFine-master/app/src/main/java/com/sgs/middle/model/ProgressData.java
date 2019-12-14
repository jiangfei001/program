package com.sgs.middle.model;

/**
 * <p>Created by 樊星(01211403) on 2018/3/1.<p>
 */
public class ProgressData {
    public int gIndex;
    public long size;
    public String desc;

    public ProgressData() {
        // ProgressData
    }

    public ProgressData(int gIndex, long size, String desc) {
        this.gIndex = gIndex;
        this.size = size;
        this.desc = desc;
    }
}
