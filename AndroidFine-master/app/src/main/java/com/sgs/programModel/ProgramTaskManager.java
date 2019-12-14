package com.sgs.programModel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgarmPalySceneVo;
import com.sgs.programModel.taskUtil.MyTask;
import com.sgs.programModel.taskUtil.PriorityTimeTask;
import com.sgs.programModel.taskUtil.TimeHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
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
            Log.e(TAG, "我是现在真正执行" + mTask.name);
            nowMyTask = mTask;
            //控制html的播放时长
            nowProgarmPalyInstructionVo = mTask.getProgarmPalyInstructionVo();
            nowProgarmPalySceneVos = JSON.parseArray(nowProgarmPalyInstructionVo.getSceneList(), ProgarmPalySceneVo.class);
            if (nowProgarmPalySceneVos != null && nowProgarmPalySceneVos.size() > 0) {
                //通知播放 控制播放时间
                //通知播放音乐
                if (nowProgarmPalyInstructionVo.getProgramMusicListArray() != null && nowProgarmPalyInstructionVo.getProgramMusicListArray().size() > 0) {
                    Event event = new Event();
                    HashMap<EventEnum, Object> params = new HashMap();
                    params.put(EventEnum.EVENT_TEST_MSG2_KEY_MUSIC, nowProgarmPalyInstructionVo.getProgramMusicListArray());
                    event.setParams(params);
                    event.setId(EventEnum.EVENT_TEST_SETMUSIC);
                    EventBus.getDefault().post(event);
                }

                startRunScence();
            }
        }

        @Override
        public void overdueTask(MyTask mTask) {
            Log.e(TAG, "我的生命周期已经到了" + mTask.name);
        }

        @Override
        public void futureTask(MyTask mTask) {
            Log.e(TAG, "未来的会执行我" + mTask.name);
        }
    };

    public void startRunScence() {
        Log.e(TAG, "startRunScence11");
        int time = sendPlayHtml(0);
        nowscene = 1;
        if (nowProgarmPalySceneVos.size() > 1) {
            Log.e(TAG, "startRunScence12:" + nowProgarmPalySceneVos.size() + "time:" + time);
            handler.sendEmptyMessageDelayed(1, time * 1000);
        } else {
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            int time1 = sendPlayHtml(nowscene);
            Log.e(TAG, "sendPlayHtml:nowscene:" + nowscene + "time1" + time1);
            if (++nowscene < nowProgarmPalySceneVos.size()) {
                Log.e(TAG, "sendPlayHtml:nowscene:" + nowscene);
            } else {
                nowscene = 0;
            }
            handler.sendEmptyMessageDelayed(1, time1 * 1000);
        }
    };


    private int sendPlayHtml(int index) {
        ProgarmPalySceneVo progarmPalySceneVo = nowProgarmPalySceneVos.get(index);
        String html = progarmPalySceneVo.getHtml();
        Log.e(TAG, "sendPlayHtml html:" + html);
        String htmlPath = "project_" + nowProgarmPalyInstructionVo.getId() + "/" + html;
        Log.e(TAG, "sendPlayHtml htmlPath:" + htmlPath);
        int time = progarmPalySceneVo.getPlayTime();
        Log.e(TAG, "sendPlayHtml time:" + time);

        Event event = new Event();
        HashMap<EventEnum, Object> params = new HashMap();
        params.put(EventEnum.EVENT_TEST_MSG2_KEY_ISPLAY_MUSIC, progarmPalySceneVo.isPalyMusic());
        params.put(EventEnum.EVENT_TEST_MSG2_KEY_HTML_PATH, htmlPath);
        event.setParams(params);
        event.setId(EventEnum.EVENT_TEST_MSG1);
        EventBus.getDefault().post(event);

        return time;
    }


    final String ACTION = "timeTask.action";
    private PriorityTimeTask<MyTask> myTaskTimeTask;

    ProgramTaskManager(Context context, LinkedList<ProgarmPalyInstructionVo> list, LinkedList<ProgarmPalyInstructionVo> priorslist) {
        // TODO: 2017/11/8  创建一个任务处理器
        myTaskTimeTask = new PriorityTimeTask<>(context, ACTION, handler);

        // TODO: 2017/11/8   添加时间回掉
        myTaskTimeTask.addHandler(timeHandler);

        // TODO: 2017/11/8  创建时间任务资源
        creatTasks(list, false);
        creatTasks(priorslist, true);

        myTaskTimeTask.startLooperTaskOrder();

    }

    public void stopLooper() {
        myTaskTimeTask.stopLooper();
    }


    public void removeByid(int id) {
        myTaskTimeTask.removeByid(id);
    }

    public void insertTask(ProgarmPalyInstructionVo progarmPalyInstructionVo, boolean exclusive) {
        MyTask bobTask = new MyTask();
        bobTask.progarmPalyInstructionVo = progarmPalyInstructionVo;
        if (exclusive) {
            myTaskTimeTask.insertPriorsTask(bobTask);
        } else {
            myTaskTimeTask.insertMTasksTask(bobTask);
        }
    }

    private void creatTasks(List<ProgarmPalyInstructionVo> list, boolean exclusive) {
        LinkedList<MyTask> mytasks = new LinkedList<MyTask>();
        for (int i = 0; i < list.size(); i++) {
            ProgarmPalyInstructionVo progarmPalyInstructionVo = list.get(i);
            MyTask bobTask = new MyTask();
            bobTask.progarmPalyInstructionVo = progarmPalyInstructionVo;
            mytasks.add(bobTask);
        }
        if (exclusive) {
            myTaskTimeTask.setPriTasks(mytasks);
        } else {
            myTaskTimeTask.setTasks(mytasks);
        }
    }
}
