package com;

import android.app.Application;
import android.content.Context;

/**
 * Created by weatherfish  (01174599) on 2016/9/27.
 */

public final class AppContext {
    private AppContext(){}

    private static Application sApp;

    public static void injectContext(Application application) {
        sApp = application;
    }

    public static final Context getAppContext() {
        return sApp;
    }
}
