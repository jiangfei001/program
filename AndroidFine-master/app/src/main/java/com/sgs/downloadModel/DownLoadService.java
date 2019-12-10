package com.sgs.downloadModel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DownLoadService extends Service {

    private static DownLoadManager downLoadManager;

    public static final String TAG = "DownloadService";

    /**
     * 需要通过bind常驻后台，防止被杀死
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "DownLoadService" + "service Bind");
        if (downLoadManager == null) {
            downLoadManager = new DownLoadManager(DownLoadService.this);
        }
        return null;
    }

    public static DownLoadManager getDownLoadManager() {
        return downLoadManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "DownLoadService" + "onCreate");
        if (downLoadManager == null) {
            downLoadManager = new DownLoadManager(DownLoadService.this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放downLoadManager
        downLoadManager.stopAllTask();
        downLoadManager = null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (downLoadManager == null) {
            downLoadManager = new DownLoadManager(DownLoadService.this);
        }
    }


}
