package com.programModel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.eventControlModel.Event;
import com.eventControlModel.EventEnum;
import com.programModel.entity.ProgarmPalyInstructionVo;
import com.programModel.entity.ProgarmPalySceneVo;
import com.programModel.taskUtil.MyTask;
import com.programModel.taskUtil.PriorityTimeTask;
import com.programModel.taskUtil.TimeHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

public class ProgramTaskManager {

    public MyTask nowMyTask = new MyTask();
    public int nowscene = 0;
    ProgarmPalyInstructionVo nowProgarmPalyInstructionVo;
    List<ProgarmPalySceneVo> nowProgarmPalySceneVos;

    public String TAG = "ProgramTaskManager";

    TimeHandler<MyTask> timeHandler = new TimeHandler<MyTask>() {
        @Override
        public void exeTask(MyTask mTask) {
            handler.removeMessages(1);
            //通知webview进行播放
            Log.e("TAG", "我是现在真正执行" + mTask.name);
            nowMyTask = mTask;
            //控制html的播放时长
            nowProgarmPalyInstructionVo = mTask.getProgarmPalyInstructionVo();
            nowProgarmPalySceneVos = JSON.parseArray(nowProgarmPalyInstructionVo.getSceneList(), ProgarmPalySceneVo.class);
            //通知播放
            //控制播放时间
            startRunScence();
        }

        @Override
        public void overdueTask(MyTask mTask) {
            Log.e("TAG", "我的生命周期已经到了" + mTask.name);
        }

        @Override
        public void futureTask(MyTask mTask) {
            Log.e("TAG", "未来的会执行我" + mTask.name);
        }
    };

    public void startRunScence() {
        int time = sendPlayHtml(0);
        nowscene = 1;
        if (nowProgarmPalySceneVos.size() > 1) {
            handler.sendEmptyMessageDelayed(1, time);
        } else {
        }

    }

    android.os.Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            int time1 = sendPlayHtml(nowscene);
            if (++nowscene < nowProgarmPalySceneVos.size()) {
                handler.sendEmptyMessageDelayed(1, time1);
            } else {
                //nowscene = 0;
            }
        }
    };


    private int sendPlayHtml(int index) {
        String html = nowProgarmPalySceneVos.get(index).getHtml();
        Log.e("TAG", "startRunScence html:" + html);
        String htmlPath = nowProgarmPalyInstructionVo.getProgramName() + "/" + html;
        Log.e("TAG", "startRunScence htmlPath:" + htmlPath);
        int time = nowProgarmPalySceneVos.get(index).getPlayTime();
        Log.e("TAG", "startRunScence time:" + time);

        Event event = new Event();
        event.setId(EventEnum.EVENT_TEST_MSG1);
        event.setPath(htmlPath);
        EventBus.getDefault().post(event);

        return time;
    }


    final String ACTION = "timeTask.action";
    private PriorityTimeTask<MyTask> myTaskTimeTask;

    ProgramTaskManager(Context context, LinkedList<ProgarmPalyInstructionVo> list) {
        // TODO: 2017/11/8  创建一个任务处理器
        myTaskTimeTask = new PriorityTimeTask<>(context, ACTION, handler);

        // TODO: 2017/11/8   添加时间回掉
        myTaskTimeTask.addHandler(timeHandler);

        // TODO: 2017/11/8  创建时间任务资源
        List<MyTask> myTasks = creatTasks(list);

        // TODO: 2017/11/8 把资源放进去处理
        myTaskTimeTask.setTasks(myTasks);
        myTaskTimeTask.startLooperTask();

    }

    public void startLooperTask() {

        myTaskTimeTask.startLooperTask();

    }

    private List<MyTask> creatTasks(List<ProgarmPalyInstructionVo> list) {
        LinkedList<MyTask> mytasks = new LinkedList<MyTask>();

        for (int i = 0; i < list.size(); i++) {
            ProgarmPalyInstructionVo progarmPalyInstructionVo = list.get(i);
            MyTask bobTask = new MyTask();
            bobTask.progarmPalyInstructionVo = progarmPalyInstructionVo;
            mytasks.add(bobTask);
        }

        return mytasks;
    }

    protected void onDestroy() {
        myTaskTimeTask.onColse();
    }
}