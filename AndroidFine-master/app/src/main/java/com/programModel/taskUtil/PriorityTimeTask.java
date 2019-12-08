package com.programModel.taskUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.programModel.entity.ProgarmPalyPlan;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


public class PriorityTimeTask<T extends MyTask> {

    private int priors = 0;
    private List<TimeHandler> mTimeHandlers = new ArrayList<TimeHandler>();
    private static PendingIntent mPendingIntent;
    private List<T> mTasks = new ArrayList<T>();
    private List<T> priorsTasks = new ArrayList<T>();
    private List<T> mTempTasks;

    String mActionName;

    private boolean isSpotsTaskIng = false;

    private Integer cursor = 0;
    private Integer priorsCursor = 0;


    private Context mContext;
    private TimeTaskReceiver receiver;

    Handler ptmHandler;

    /**
     * @param mContext
     * @param actionName action不要重复
     */
    public PriorityTimeTask(Context mContext, String actionName, Handler handler) {
        this.mContext = mContext;
        this.mActionName = actionName;
        initBreceiver(mContext);
        this.ptmHandler = handler;
    }

    private void initBreceiver(Context mContext) {
        receiver = new TimeTaskReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(mActionName);
        mContext.registerReceiver(receiver, filter);
    }


    public void setTasks(List<T> mES) {
        cursorInit();
        if (mTempTasks != null) {
            mTempTasks = mES;
        } else {
            this.mTasks = mES;
        }
    }

    public void setPriorityTasks(List<T> priorityTasks) {
        cursorInit();
       /* if (mTempTasks != null) {
            mTempTasks = mES;
        } else {
            this.mTasks = mES;
        }*/
    }

    /**
     * 恢复普通任务
     */
    private void resetTask() {
        synchronized (mTasks) {
            isSpotsTaskIng = false;
            if (mTempTasks != null) {//有发生过插播
                mTasks = mTempTasks;
                mTempTasks = null;
                cancelAlarmManager();
                cursorInit();
                startLooperTask();
            }
            mHandler.removeMessages(1);
        }
    }


    /**
     * 任务计数归零
     */
    private void cursorInit() {
        cursor = 0;
    }

    private void cursorPriorityInit() {
        priorsCursor = 0;
    }

    /**
     * 添加任务监听
     *
     * @param mTH
     * @return
     */
    public PriorityTimeTask addHandler(TimeHandler<T> mTH) {
        mTimeHandlers.add(mTH);
        return this;
    }


    //新建Handler对象。
    Handler mHandler = new Handler() {
        //handleMessage为处理消息的方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //启动下一次任务
            startLooperTask();
            return;
        }
    };

    public boolean doneLooper(List<T> tasklist, Integer cursor) {
        ptmHandler.removeMessages(1);
        if (tasklist.size() > cursor) {
        } else {
            cursor = 0;
        }
        long mNowtime = System.currentTimeMillis();
        //循环开始为游标的位置，循环所有任务的大小，游标等于列表的大小时，游标记录为0
        for (int i = 0; i < tasklist.size(); i++) {
            T mTask = tasklist.get(cursor);
            cursor++;
            if (tasklist.size() == cursor) { //恢复普通任务
                cursor = 0;
            }
            if (mTask.progarmPalyInstructionVo.getTotalStatus() == 1) {
                List<ProgarmPalyPlan> progarmPalyPlan = mTask.progarmPalyInstructionVo.getPublicationPlanObject().getOkProgarms();
                for (int t = 0; t < progarmPalyPlan.size(); t++) {
                    ProgarmPalyPlan progarmPalyPlan1 = progarmPalyPlan.get(t);
                    if (progarmPalyPlan1.getStartTime() < mNowtime && progarmPalyPlan1.getEndTime() > mNowtime) {
                        //在当前区间内立即执行
                        for (TimeHandler mTimeHandler : mTimeHandlers) {
                            mTimeHandler.exeTask(mTask);
                            //预设下一个节目播放
                            mHandler.sendEmptyMessageDelayed(1, mTask.getEndTime() - mTask.getStarTime());
                        }
                        return true;
                    }
                }

            }
        }

        return false;
    }

    public boolean isRuning = false;

    /**
     * 开始任务
     */
    public void startLooperTask() {
        if (priorsTasks.size() > 0 || mTasks.size() > 0) {
            isRuning = true;
            boolean idone = doneLooper(priorsTasks, priorsCursor);
            if (idone) {
                doneLooper(mTasks, cursor);
            }
        }
    }


    /**
     * 停止任务
     */
    public void stopLooper() {
        cancelAlarmManager();
    }

    /**
     * 装在定时任务
     *
     * @param Time
     */
    private void configureAlarmManager(long Time) {
        AlarmManager manager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        PendingIntent pendIntent = getPendingIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Time, pendIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, Time, pendIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, Time, pendIntent);
        }
    }

    /**
     * 取消定时器
     */
    private void cancelAlarmManager() {
        AlarmManager manager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        manager.cancel(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        if (mPendingIntent == null) {
            int requestCode = 0;
            Intent intent = new Intent();
            intent.setAction(mActionName);
            /* Intent intent = new Intent(mContext,TimeTaskReceiver.class);*/
            mPendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, 0);
        }
        return mPendingIntent;
    }

    /**
     * 插播任务
     */
    public void spotsTask(List<T> mSpotsTask) {
        // 2017/10/16 暂停 任务分发
        isSpotsTaskIng = true;
        synchronized (mTasks) {
            if (mTempTasks == null && mTasks != null) {//没有发生过插播
                mTempTasks = new ArrayList<T>();
                for (T mTask : mTasks) {
                    mTempTasks.add(mTask);
                }
            }
            mTasks = mSpotsTask;
            //  2017/10/16 恢复 任务分发
            cancelAlarmManager();
            cursorInit();
            startLooperTask();
        }
    }

    /**
     * 恢复普通任务
     */
    private void recoveryTask() {
        synchronized (mTasks) {
            isSpotsTaskIng = false;
            if (mTempTasks != null) {//有发生过插播
                mTasks = mTempTasks;
                mTempTasks = null;
                cancelAlarmManager();
                cursorInit();
                startLooperTask();
            }
        }
    }

    public void onColse() {
        mContext.unregisterReceiver(receiver);
        mContext = null;
    }

    public class TimeTaskReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("context", "context");
            //判断比自己大的优先级 队列有没有需要执行的
            PriorityTimeTask.this.startLooperTask(); //预约下一个
        }
    }


}
