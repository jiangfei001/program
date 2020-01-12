package com.sgs.programModel.taskUtil;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sgs.programModel.entity.ProgarmPalyPlan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PriorityTimeTask<T extends MyTask> {

    public String TAG = "PriorityTimeTask";

    private int priors = 0;
    private List<TimeHandler> mTimeHandlers = new ArrayList<TimeHandler>();
    private static PendingIntent mPendingIntent;

    private List<T> dTasks = new ArrayList<T>();
    private List<T> mTasks = new ArrayList<T>();
    private List<T> priorsTasks = new ArrayList<T>();
    private List<T> mTempTasks;


    String mActionName;

    private boolean isSpotsTaskIng = false;

    private Integer dcursor = 0;
    private Integer cursor = 0;
    private Integer priorsCursor = 0;

    private Context mContext;

    Handler ptmHandler;

    /**
     * @param mContext
     * @param actionName action不要重复
     */
    public PriorityTimeTask(Context mContext, String actionName, Handler handler) {
        this.mContext = mContext;
        this.mActionName = actionName;
        this.ptmHandler = handler;
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

    private void cursorDInit() {
        dcursor = 0;
    }


    public void insertTaskByPri(T bobTask, PRI pRi) {
        if (PRI.TASK_PRI == pRi) {
            if (priorsTasks == null) {
                priorsTasks = new LinkedList<>();
            }
            priorsTasks.add(bobTask);
            Log.e(TAG, "我是临时插入进来的一个高优先级的节目");

        } else if (PRI.TASK_NOR == pRi) {
            if (mTasks == null) {
                mTasks = new LinkedList<>();
            }
            mTasks.add(bobTask);
            Log.e(TAG, "我是临时插入进来的一个普通优先级的节目");
        } else {
            if (dTasks == null) {
                dTasks = new LinkedList<>();
            }
            dTasks.add(bobTask);
            Log.e(TAG, "我是临时插入进来的一个低级优先级的节目");
        }
        insertStartLooperTask();
    }

    public void insertPriorsTask(T bobTask) {
        if (priorsTasks == null) {
            priorsTasks = new LinkedList<>();
        }
        priorsTasks.add(bobTask);
        Log.e(TAG, "我是临时插入进来的一个高优先级的节目");
        insertStartLooperTask();
    }


    public void setTasks(List<T> mES) {
        cursorInit();
        this.mTasks = mES;
    }

    public void setPriTasks(List<T> mES) {
        cursorPriorityInit();
        this.priorsTasks = mES;
    }


    public void setDTasks(List<T> mES) {
        cursorDInit();
        this.dTasks = mES;
    }


    public void removeByid(int id) {
        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).progarmPalyInstructionVo.getId() == id) {
                mTasks.remove(i);
                break;
            }
        }
        for (int i = 0; i < priorsTasks.size(); i++) {
            if (priorsTasks.get(i).progarmPalyInstructionVo.getId() == id) {
                priorsTasks.remove(i);
                break;
            }
        }
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
            Log.e(TAG, "进入下一次开始执行startLooperTask");
            startLooperTaskOrder();
            return;
        }
    };

    public boolean doneLooper(List<T> tasklist, PRI isPri) {
        if (isPri == PRI.TASK_NOR) {
            if (tasklist.size() > cursor) {
            } else {
                cursor = 0;
            }
        } else if (isPri == PRI.TASK_PRI) {
            if (tasklist.size() > priorsCursor) {
            } else {
                priorsCursor = 0;
            }
        } else if (isPri == PRI.TASK_D) {
            if (tasklist.size() > dcursor) {
            } else {
                dcursor = 0;
            }
        }

        long mNowtime = System.currentTimeMillis();
        //循环开始为游标的位置，循环所有任务的大小，游标等于列表的大小时，游标记录为0
        for (int i = 0; i < tasklist.size(); i++) {
            T mTask = null;
            if (isPri == PRI.TASK_NOR) {
                mTask = tasklist.get(cursor);
                cursor++;
                if (tasklist.size() < cursor) { //恢复普通任务
                    cursor = 0;
                }
                Log.e(TAG, "cursor" + cursor);
            } else if (isPri == PRI.TASK_PRI) {
                mTask = tasklist.get(priorsCursor);
                priorsCursor++;
                if (tasklist.size() < priorsCursor) { //恢复普通任务
                    priorsCursor = 0;
                }
                Log.e(TAG, "priorsCursor" + priorsCursor + "tasklist.size()" + tasklist.size());
            } else if (isPri == PRI.TASK_D) {
                mTask = tasklist.get(dcursor);
                dcursor++;
                if (tasklist.size() < dcursor) { //恢复普通任务
                    dcursor = 0;
                }
                Log.e(TAG, "norCursor" + dcursor + "tasklist.size()" + tasklist.size());
            }
            if (mTask.progarmPalyInstructionVo.getTotalStatus() == 1) {
                List<ProgarmPalyPlan> progarmPalyPlan = mTask.progarmPalyInstructionVo.getPublicationPlanObject().getOkProgarms();
                for (int t = 0; t < progarmPalyPlan.size(); t++) {
                    ProgarmPalyPlan progarmPalyPlan1 = progarmPalyPlan.get(t);
                    if (progarmPalyPlan1.getStartTime() < mNowtime && progarmPalyPlan1.getEndTime() > mNowtime) {
                        //预设下一个节目播放
                        Log.e(TAG, "下一次节目判断" + mTask.progarmPalyInstructionVo.getPlayTime());
                        mHandler.sendEmptyMessageDelayed(1, mTask.progarmPalyInstructionVo.getPlayTime() * 1000);
                        //在当前区间内立即执行
                        for (TimeHandler mTimeHandler : mTimeHandlers) {
                            mTimeHandler.exeTask(mTask);
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
    public void insertStartLooperTask() {
        if (!isRuning) {
            order();
        }
    }

    public void startLooperTaskOrder() {
        order();
    }

    private void order() {
        if ((priorsTasks != null && priorsTasks.size() > 0) || (mTasks != null && mTasks.size() > 0) || (dTasks != null && dTasks.size() > 0)) {
            boolean idone = false;
            if (priorsTasks != null && priorsTasks.size() > 0) {
                idone = doneLooper(priorsTasks, PRI.TASK_PRI);
            }

            if (!idone) {
                if (mTasks != null && mTasks.size() > 0) {
                    idone = doneLooper(mTasks, PRI.TASK_NOR);
                }
            }

            if (!idone) {
                if (dTasks != null && dTasks.size() > 0) {
                    idone = doneLooper(dTasks, PRI.TASK_D);
                }
            }

            if (idone) {
                isRuning = true;
            }
        }
    }

    /**
     * 停止任务
     */
    public void stopLooper() {
        priorsTasks = null;
        mTasks = null;
        dTasks = null;
        Log.e(TAG, "被移除 stopLooper");
        mHandler.removeMessages(1);
    }

}
