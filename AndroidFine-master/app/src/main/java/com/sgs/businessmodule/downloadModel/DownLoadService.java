package com.sgs.businessmodule.downloadModel;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sgs.AppContext;
import com.sgs.programModel.ProgramScheduledManager;

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
        ProgramScheduledManager programScheduledManager = ProgramScheduledManager.getInstance();
        return mBinder;
    }

    IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public DownLoadService getServerInstance() {
            return DownLoadService.this;
        }
    }

    public static DownLoadManager getDownLoadManager() {
        if (downLoadManager == null) {
            downLoadManager = new DownLoadManager(AppContext.getInstance());
        }
        return downLoadManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "DownLoadService" + "onCreate");
        if (downLoadManager == null) {
            downLoadManager = new DownLoadManager(DownLoadService.this);
            ProgramScheduledManager programScheduledManager = ProgramScheduledManager.getInstance();
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
