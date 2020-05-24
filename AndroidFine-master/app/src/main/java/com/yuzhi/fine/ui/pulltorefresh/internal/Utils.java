package com.yuzhi.fine.ui.pulltorefresh.internal;

import com.zhangke.zlog.ZLog;

public class Utils {

    static final String LOG_TAG = "PullToRefresh";

    public static void warnDeprecation(String depreacted, String replacement) {
        ZLog.e(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
    }

}
