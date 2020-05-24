package com.zhangke.zlog;

import com.zhangke.zlog.ZLog;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Singleton class.</p>
 * Created by ZhangKe on 2018/1/11.
 */

class LogQueue {

    private static final String TAG = "LogQueue";

    /**
     * 存储日志的队列
     */
    private LinkedBlockingQueue<LogBean> mLogQueue = new LinkedBlockingQueue<>();

    public LogDispatcher getmLogDispatcher() {
        return mLogDispatcher;
    }

    private LogDispatcher mLogDispatcher;

    LogQueue(String logDir){
        mLogDispatcher = new LogDispatcher(mLogQueue, logDir);
    }

    void start(){
        mLogDispatcher.start();
    }

    void add(final LogBean logBean){
        try {
            boolean b = mLogQueue.offer(logBean);
            if (!b) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mLogQueue.put(logBean);
                        } catch (InterruptedException e) {
                            ZLog.e(TAG, "run: ", e);
                        }
                    }
                }).start();
            }
        }catch(Exception e){
            ZLog.e(TAG, "add: ", e);
        }
    }
}
