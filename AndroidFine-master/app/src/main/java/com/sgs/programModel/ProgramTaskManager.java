package com.sgs.programModel;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.uiModel.loginUtil.LoginUtil;
import com.zhangke.zlog.ZLog;

import com.alibaba.fastjson.JSON;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.ScenceReportRequestManager;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgarmPalySceneVo;
import com.sgs.programModel.taskUtil.MyTask;
import com.sgs.programModel.taskUtil.PRI;
import com.sgs.programModel.taskUtil.PriorityTimeTask;
import com.sgs.programModel.taskUtil.TimeHandler;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProgramTaskManager {

    public MyTask nowMyTask = new MyTask();
    public int nowscene = 0;
    public ProgarmPalyInstructionVo nowProgarmPalyInstructionVo;

    public ProgarmPalyInstructionVo getNowProgarmPalyInstructionVo() {
        return nowProgarmPalyInstructionVo;
    }

    public List<ProgarmPalySceneVo> nowProgarmPalySceneVos;

    public String TAG = "ProgramTaskManager";


    public void clearAll() {
        stopLooper();
        handler.removeMessages(1);
        nowProgarmPalySceneVos = null;
        nowscene = 0;
        nowProgarmPalyInstructionVo = null;
    }

    //接受task的返回
    TimeHandler<MyTask> timeHandler = new TimeHandler<MyTask>() {
        @Override
        public void exeTask(MyTask mTask) {
            handler.removeMessages(1);
            ZLog.e(TAG, "我是现在真正执行exeTask");
            //通知webview进行播放 整个节目
            nowMyTask = mTask;
            //控制html的播放时长
            nowProgarmPalyInstructionVo = mTask.getProgarmPalyInstructionVo();
            //获取所有的场景
            nowProgarmPalySceneVos = JSON.parseArray(nowProgarmPalyInstructionVo.getSceneList(), ProgarmPalySceneVo.class);
            if (nowProgarmPalySceneVos != null && nowProgarmPalySceneVos.size() > 0) {
                ZLog.e(TAG, "我是现在真正执行" + nowProgarmPalyInstructionVo.getProgramName());
                //通知播放 控制播放时间
                //将音乐丢给前端进行循环播放
                if (nowProgarmPalyInstructionVo.getProgramMusicListArray() != null && nowProgarmPalyInstructionVo.getProgramMusicListArray().size() > 0) {
                    ZLog.e(TAG, "音乐" + nowProgarmPalyInstructionVo.getProgramMusicListArray().size() + "size");
                    Event event = new Event();
                    HashMap<EventEnum, Object> params = new HashMap();
                    params.put(EventEnum.EVENT_TEST_MSG2_KEY_MUSIC, nowProgarmPalyInstructionVo.getProgramMusicListArray());
                    event.setParams(params);
                    event.setId(EventEnum.EVENT_TEST_SETMUSIC);
                    EventBus.getDefault().post(event);
                } else {
                    //清空背景音乐
                    ZLog.e(TAG, "音乐清空" + nowProgarmPalyInstructionVo.getProgramMusicListArray() + "");
                    Event event = new Event();
                    HashMap<EventEnum, Object> params = new HashMap();
                    params.put(EventEnum.EVENT_TEST_MSG2_KEY_MUSIC, null);
                    event.setParams(params);
                    event.setId(EventEnum.EVENT_TEST_SETMUSIC);
                    EventBus.getDefault().post(event);
                }
                startRunScence();
            }
        }

        @Override
        public void overdueTask(MyTask mTask) {
            ZLog.e(TAG, "我的生命周期已经到了" + mTask.name);
        }

        @Override
        public void futureTask(MyTask mTask) {
            ZLog.e(TAG, "未来的会执行我" + mTask.name);
        }
    };

    public void startRunScence() {
        ZLog.e(TAG, "startRunScence11");
        int time = sendPlayHtml(0);
        if (nowProgarmPalySceneVos.size() > 1) {
            nowscene = 1;
            ZLog.e(TAG, "startRunScence12:" + nowProgarmPalySceneVos.size() + "time:" + time);
            handler.sendEmptyMessageDelayed(1, time * 1000);
        } else {
            saveScenceReport(time);
        }
    }


    public

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(final Message msg) {
            int time1 = sendPlayHtml(nowscene);
            ZLog.e(TAG, "sendPlayHtml:nowscene:" + nowscene + "time1" + time1);
            //按日期保存更新场景id
            saveScenceReport(time1);
            if (++nowscene < nowProgarmPalySceneVos.size()) {
                ZLog.e(TAG, "sendPlayHtml:nowscene:" + nowscene);
            } else {
                nowscene = 0;
            }
            handler.sendEmptyMessageDelayed(1, time1 * 1000);
        }
    };

    private void saveScenceReport(int time1) {
        String nowDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        ScenceReport scenceReport = ScenceReportRequestManager.getInstance().queryByDateAndScenceId(nowProgarmPalySceneVos.get(nowscene).getSceneId(), nowDate);
        ZLog.e(TAG, "sendPlayHtml:scenceReport:" + scenceReport);

        if (scenceReport == null) {
            scenceReport = new ScenceReport();
            scenceReport.setPalyDate(nowDate);
            scenceReport.setPalySecond(time1);
            scenceReport.setPalyNum(1);
            scenceReport.setProgramId(nowProgarmPalyInstructionVo.getId() + "");
            scenceReport.setTerminalIdentity(LoginUtil.getTerminalIdentity());
            scenceReport.setTerminalName(LoginUtil.getTerminalIdentity());
            scenceReport.setProgramName(nowProgarmPalyInstructionVo.getProgramName());
            scenceReport.setSceneName(nowProgarmPalySceneVos.get(nowscene).getSceneName());
            scenceReport.setSceneId(nowProgarmPalySceneVos.get(nowscene).getSceneId());
            ZLog.e(TAG, "scenceReport:" + scenceReport.toString());
        } else {
            scenceReport.setPalySecond(scenceReport.getPalySecond() + time1);
            scenceReport.setPalyNum(scenceReport.getPalyNum() + 1);
            ZLog.e(TAG, "scenceReport:" + scenceReport.toString());
        }
        ScenceReportRequestManager.getInstance().saveInstructionRequest(scenceReport);
    }


    private int sendPlayHtml(int index) {
        ProgarmPalySceneVo progarmPalySceneVo = nowProgarmPalySceneVos.get(index);
        String html = progarmPalySceneVo.getHtml();
        ZLog.e(TAG, "sendPlayHtml html:" + html);
        String htmlPath = "project_" + nowProgarmPalyInstructionVo.getId() + "/" + html;
        ZLog.e(TAG, "sendPlayHtml htmlPath:" + htmlPath);
        int time = progarmPalySceneVo.getPlayTime();
        ZLog.e(TAG, "sendPlayHtml time:" + time);

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

    private Context context;

    ProgramTaskManager(Context context, LinkedList<ProgarmPalyInstructionVo> list, LinkedList<ProgarmPalyInstructionVo> priorslist, LinkedList<ProgarmPalyInstructionVo> dlist) {
        // TODO: 2017/11/8  创建一个任务处理器
        this.context = context;
        myTaskTimeTask = new PriorityTimeTask<>(context, ACTION, handler);

        // TODO: 2017/11/8   添加时间回掉
        myTaskTimeTask.addHandler(timeHandler);

        // TODO: 2017/11/8  创建时间任务资源 0： 循环播放  1：按周播放  2：自定义播放
        creatTasks(list, PRI.TASK_NOR);
        creatTasks(priorslist, PRI.TASK_PRI);
        creatTasks(dlist, PRI.TASK_D);


        myTaskTimeTask.startLooperTaskOrder();

    }


    public void stopLooper() {
        myTaskTimeTask.stopLooper();
    }


    public void removeByid(int id) {
        if (nowProgarmPalyInstructionVo != null && nowProgarmPalyInstructionVo.getId() == id) {
            handler.removeMessages(1);
            nowProgarmPalySceneVos = null;
            nowscene = 0;
            nowProgarmPalyInstructionVo = null;
        }
        myTaskTimeTask.removeByid(id);
    }

    public void insertTask(ProgarmPalyInstructionVo progarmPalyInstructionVo, PRI exclusive) {
        MyTask bobTask = new MyTask();
        bobTask.progarmPalyInstructionVo = progarmPalyInstructionVo;
        ZLog.e(TAG, "开始插入一个优先级" + exclusive);
        myTaskTimeTask.insertTaskByPri(bobTask, exclusive);
    }

    private void creatTasks(List<ProgarmPalyInstructionVo> list, PRI prilevel) {
        LinkedList<MyTask> mytasks = new LinkedList<MyTask>();
        for (int i = 0; i < list.size(); i++) {
            ProgarmPalyInstructionVo progarmPalyInstructionVo = list.get(i);
            MyTask bobTask = new MyTask();
            bobTask.progarmPalyInstructionVo = progarmPalyInstructionVo;
            mytasks.add(bobTask);
        }

        if (prilevel == PRI.TASK_D) {
            myTaskTimeTask.setDTasks(mytasks);
        } else if (prilevel == PRI.TASK_NOR) {
            myTaskTimeTask.setTasks(mytasks);
        } else if (prilevel == PRI.TASK_PRI) {
            myTaskTimeTask.setPriTasks(mytasks);
        }
    }

    public PriorityTimeTask<MyTask> getMyTaskTimeTask() {
        return myTaskTimeTask;
    }

}
