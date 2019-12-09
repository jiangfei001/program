package com.downloadModel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.programModel.ProgramScheduledManager;

public class DownLoadService extends Service {
    private static DownLoadManager downLoadManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public static DownLoadManager getDownLoadManager() {
        return downLoadManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("DownLoadService", "DownLoadService" + "onCreate");
        downLoadManager = new DownLoadManager(DownLoadService.this);
        ProgramScheduledManager programScheduledManager = ProgramScheduledManager.getInstance();
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
