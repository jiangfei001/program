package com.sgs.programModel.programSchedule.scheduledMangaer;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.zhangke.zlog.ZLog;
import android.view.View;

import com.sgs.programModel.programSchedule.util.AlarmUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 汇总5种常见实现定时器的原理
 * 1 thread+sleep
 * 2 Handler.postDelayed(Runnable, long)
 * 3（单thread）Timer+TimerTask
 * 4 ScheduledExecutorService + TimerTask方式（多线程 ）
 * 5 AlarmManager实现精确定时
 */
public class scheduledHelper {
    private static final long HEART_BEAT_RATE = 30 * 1000;//目前心跳检测频率为30s
    public static final String INTENT_BROADCAST_ALARM_ACTION = "intent_filter_action";
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            // excute task
            processTask();
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            processTask();
        }
    };
    private Timer mTimer = new Timer();

    private ScheduledExecutorService mThreadPool = Executors.newSingleThreadScheduledExecutor();
    private boolean isSetAlarm = true;
    private Thread mThread;

    private void processTask() {
        ZLog.e("processTask", "----execute task");
    }

    /* @Override
     protected void onPause() {
         super.onPause();
         if (mHandler != null) {
             mHandler.removeCallbacks(heartBeatRunnable);
         }

         mTimer.cancel();
     }
 */
    public void onHandler(View v) {
        if (mHandler != null) {
            mHandler.removeCallbacks(heartBeatRunnable);
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        }
    }

    public void onSleep(View v) {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mThread.isInterrupted()) {
                    processTask();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mThread.start();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mThread.interrupt();
            }
        }, 10000);
    }

    public void onTimer(View v) {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 1000);

    }

    public void onScheduledExecutorService(View v) {
        if (mThreadPool != null && mThreadPool.isShutdown()) {
            mThreadPool.shutdown();
        }
        mThreadPool.schedule(mTimerTask, 1000, TimeUnit.MILLISECONDS);

    }

    public void onAlarmManager(Context context) {
        /* Intent intent = new Intent(INTENT_BROADCAST_ALARM_ACTION);*/
        Intent intent = new Intent(context, AlarmReceiver.class);
        if (isSetAlarm) {
            isSetAlarm = false;
            long warmTime = System.currentTimeMillis() + 10;
            AlarmUtil.setAlarm(context, warmTime * 1000, intent);

        } else {
            isSetAlarm = true;
            AlarmUtil.cancelAlarm(context, intent);
        }


       /* Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5*1000), pendingIntent);*/

    }

  /*  public void onRxjava(View view) {
        Toast.makeText(this, "java8支持，可是暂时不支持Android", Toast.LENGTH_SHORT).show();
    }

    public void onChronometer(View v) {
        Toast.makeText(this, "https://developer.android.com/reference/android/widget/Chronometer.html", Toast.LENGTH_SHORT).show();
    }

    public void onCountDownTimer(View v) {
        Toast.makeText(this, "https://developer.android.com/reference/android/os/CountDownTimer.html", Toast.LENGTH_SHORT).show();
    }

    public void onCustomChronometer(View v) {
        startActivity(new Intent(WebSocketActivity.this, ChronometerActivity.class));
    }

    public void onTicker(View v) {
        Toast.makeText(this, "https://github.com/robinhood/ticker.git", Toast.LENGTH_SHORT).show();
    }*/

}
