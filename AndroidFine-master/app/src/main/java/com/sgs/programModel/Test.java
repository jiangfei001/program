package com.sgs.programModel;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.sgs.programModel.taskUtil.MyTask;
import com.sgs.programModel.taskUtil.PriorityTimeTask;
import com.sgs.programModel.taskUtil.TimeHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Test {

    TimeHandler<MyTask> timeHandler = new TimeHandler<MyTask>() {
        @Override
        public void exeTask(MyTask mTask) {
            //通知webview进行播放
            Log.d("TimeTask11", "我是现在真正执行" + mTask.name);
        }

        @Override
        public void overdueTask(MyTask mTask) {
            Log.d("TimeTask11", "我的生命周期已经到了" + mTask.name);
        }

        @Override
        public void futureTask(MyTask mTask) {
            Log.d("TimeTask11", "未来的会执行我" + mTask.name);
        }
    };
    final String ACTION = "timeTask.action";
    private PriorityTimeTask<MyTask> myTaskTimeTask;

    protected void onCreate(Context context) {
        // TODO: 2017/11/8  创建一个任务处理器
        myTaskTimeTask = new PriorityTimeTask<>(context, ACTION, new Handler());

        // TODO: 2017/11/8   添加时间回掉
        myTaskTimeTask.addHandler(timeHandler);

        // TODO: 2017/11/8  创建时间任务资源
        List<MyTask> myTasks = creatTasks();

        // TODO: 2017/11/8 把资源放进去处理
        myTaskTimeTask.setTasks(myTasks);
        myTaskTimeTask.startLooperTaskOrder();

    }

    private List<MyTask> creatTasks() {
        return new ArrayList<MyTask>() {{
            MyTask BobTask = new MyTask();
            BobTask.setStarTime(dataOne("2019-10-26 12:01:00"));   //当前时间
            BobTask.setEndTime(dataOne("2019-10-26  12:58:00"));  //5秒后结束
            BobTask.name = "Bob";
            add(BobTask);
/*
            MyTask benTask = new MyTask();
            BobTask.setStarTime(dataOne("2019-10-26 19:13:00"));   //当前时间
            BobTask.setEndTime(dataOne("2019-10-26  19:25:00"));  //5秒后结束
            benTask.name = "Ben";
            add(benTask);*/
        }};
    }

    public static long dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            Log.e("TimeTask", "date" + date);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.parseLong(times) * 1000;
    }

}
