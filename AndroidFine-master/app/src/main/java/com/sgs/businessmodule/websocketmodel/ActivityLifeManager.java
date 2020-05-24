package com.sgs.businessmodule.websocketmodel;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.zhangke.zlog.ZLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

public class ActivityLifeManager implements Application.ActivityLifecycleCallbacks {

    //此处用双端链表是因为存在一种场景为界面A create,界面B 还没来得及destory，此时会判断不准确，所以取队列尾部的数据。
    private LinkedBlockingDeque<Activity> mCreateAndDesotryActivitys = new LinkedBlockingDeque<>();

    //此线程安全的list用来记录activity resume和stop，用于判断应用是否在前台,因为会频繁写，所以效率会优于copyOnWriteArray
    private List<Activity> mResumeAndStopActivitys = Collections.synchronizedList(new ArrayList<Activity>());
    private static WeakReference<Activity> sActivity;

    /**
     * App前后台状态
     */
    public boolean isForeground = false;

    private AppStatusListener mlistener = null;

    private ActivityLifeManager() {

    }

    public static ActivityLifeManager getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final ActivityLifeManager sInstance = new ActivityLifeManager();
    }

    /**
     * 获取当前显示的activity
     */
    public Activity currentActivity() {
        try {
            if (!mCreateAndDesotryActivitys.isEmpty()) {
                Activity activityLast = mCreateAndDesotryActivitys.getLast();
                return activityLast;//队列尾部才是最新的activity
            }
        } catch (NoSuchElementException e) {
            ZLog.e("ActivityLifeManager", " OverallDialog currentActivity error = " + e.getMessage());
        }
        return null;
    }

    public Activity getActivity() {
        return sActivity.get();
    }

    public void setAppStatusListener(AppStatusListener listener) {
        this.mlistener = listener;
    }

    /**
     * 判断app是否在前台
     */
    public boolean isAppFront() {
        return mResumeAndStopActivitys.size() > 0;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (!mCreateAndDesotryActivitys.contains(activity)) {
            mCreateAndDesotryActivitys.add(activity); //FIFO 将界面add到尾部
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!mResumeAndStopActivitys.contains(activity)) {
            mResumeAndStopActivitys.add(activity);
        }
        sActivity = new WeakReference<>(activity);
        if (isForeground == false) {
            //由后台切换到前台
            isForeground = true;
            if (mlistener != null) {
                mlistener.goForeground();
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        try {
            if (mResumeAndStopActivitys.contains(activity)) {
                mResumeAndStopActivitys.remove(activity);
            }
            if (!isAppFront()) {
                //由前台切换到后台
                isForeground = false;
                if (mlistener != null) {
                    mlistener.goBackgroud();
                }
            }
        } catch (Exception e) {
            ZLog.e("ActivityLifeManager", " OverallDialog onActivityStopped error = " + e.getMessage());
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        try {
            if (mCreateAndDesotryActivitys.contains(activity)) {
                mCreateAndDesotryActivitys.remove(activity);
            }
        } catch (Exception e) {
            ZLog.e("ActivityLifeManager", " OverallDialog onActivityDestroyed error = " + e.getMessage());
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        //输出所有缓存的日志

        while (!mCreateAndDesotryActivitys.isEmpty()) {
            Activity pop = mCreateAndDesotryActivitys.pop();
            pop.finish();
        }
        mCreateAndDesotryActivitys.clear();
    }

    /**
     * 重启应用程序
     */
    public void restartAPP(Context context) {
        ZLog.e("ActivityLifeManager", "restartAPP");
        finishAllActivity();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, 5000, restartIntent); // 1秒钟后重启应用
        exitApp(context);
    }

    /**
     * 退出应用程序
     */
    public void exitApp(Context context) {

        try {
            ZLog.e("ActivityLifeManager", "exit by application");
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            activityMgr.killBackgroundProcesses(context.getPackageName() + ":remote");
        } catch (Exception e) {
            ZLog.e("ActivityLifeManager", e.getMessage(), e);
        }

        System.exit(0);
    }

    public interface AppStatusListener {
        void goForeground();

        void goBackgroud();
    }
}



