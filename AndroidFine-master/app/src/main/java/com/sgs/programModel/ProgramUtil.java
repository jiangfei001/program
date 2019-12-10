package com.sgs.programModel;

import android.util.Log;

import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgarmPalyPlan;
import com.sgs.programModel.entity.ProgarmPalySchedule;
import com.sgs.programModel.entity.PublicationPlanVo;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ProgramUtil {

    public static String TAG = "ProgramUtil";

    //判断Week&&自定义的时间段，今天是否播放，今天播放的有几个时间段
    public static boolean getWeekPalySchedule(ProgarmPalyInstructionVo progarmPalyInstructionVo) {

        PublicationPlanVo publicationPlanVo = progarmPalyInstructionVo.getPublicationPlanObject();

        List<ProgarmPalyPlan> okProgarms = new LinkedList<>();

        publicationPlanVo.setOkProgarms(okProgarms);

        //判断week是否在今天
        List<ProgarmPalySchedule> progarmPalySchedules = publicationPlanVo.getWeekPalySchedule();

        boolean isTodayPlay = false;
        if (progarmPalySchedules != null && progarmPalySchedules.size() > 0) {
            Date date = new Date();
            String week = getWeekOfDate(date);
            Log.e(TAG, "week:" + week);
            for (int i = 0; i < progarmPalySchedules.size(); i++) {
                ProgarmPalySchedule progarmPalySchedule = progarmPalySchedules.get(i);
                if (week.equals(progarmPalySchedule.getDateStr())) {
                    isTodayPlay = true;
                    Log.e(TAG, "weekProgarmPalySchedules:" + i);
                    //"times": "08:00-10:00|12:00-16:00"
                    String timesD = progarmPalySchedule.getTimes();
                    Log.e(TAG, "timesD" + timesD);
                    String a[] = timesD.split("\\|");
                    //08:00-10:00
                    for (int t = 0; t < a.length; t++) {
                        String timetemp = a[t];
                        Log.e(TAG, "timetemp" + timetemp);
                        if (timetemp.indexOf("-") != -1) {
                            String b[] = timetemp.split("-");
                            ProgarmPalyPlan progarmPalyPlan = new ProgarmPalyPlan();
                            //08:00
                            progarmPalyPlan.setStartTime(dataOne(b[0]));
                            progarmPalyPlan.setEndTime(dataOne(b[1]));

                            okProgarms.add(progarmPalyPlan);
                        }
                    }
                }
            }
        }

        //判断自定义的时间段
        List<ProgarmPalySchedule> customizedPalySchedule = publicationPlanVo.getCustomizedPalySchedule();

        if (progarmPalySchedules != null && progarmPalySchedules.size() > 0) {

        }

        return isTodayPlay;

    }

    public static void main(String[] args) {
        String timesD = "13:03-13:12";
        // System.out.println("timesD" + timesD);

        String a[] = timesD.split("\\|");

        //08:00-10:00

        for (int t = 0; t < a.length; t++) {

            String timetemp = a[t];

            // System.out.println("timetemp" + timetemp);

            if (timetemp.indexOf("-") != -1) {

                String b[] = timetemp.split("-");

                ProgarmPalyPlan progarmPalyPlan = new ProgarmPalyPlan();
                //08:00
                progarmPalyPlan.setStartTime(dataOne(b[0]));

                progarmPalyPlan.setEndTime(dataOne(b[1]));
            }
        }
    }

    public static long dataOne(String time) {
       /* SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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
        return Long.parseLong(times) * 1000;*/

        String k[] = time.split(":");
        Date now = new Date();

        String week = getWeekOfDate(now);
        System.out.println(week);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);

        System.out.printf("%1$tF %1$tT\n", cal1.getTime());// cal1.getTime()返回的Date已经是更新后的对象

        long ts1 = now.getTime();
        System.out.println(ts1);


        // 将时分秒,毫秒域清零
        cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(k[0]));
        cal1.set(Calendar.MINUTE, Integer.parseInt(k[1]));
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        System.out.printf("%1$tF %1$tT\n", cal1.getTime());// cal1.getTime()返回的Date已经是更新后的对象

        Date date = cal1.getTime();
        long ts = date.getTime();
        System.out.println(ts);

        return ts;
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

}
