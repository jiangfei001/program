package com.sgs;

import com.zhangke.zlog.ZLog;

import com.sgs.businessmodule.httpModel.MyApiResponse;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.RedHotReportRequestManager;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.ScenceReportRequestManager;
import com.sgs.businessmodule.upReportModel.RepHotReport;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.programModel.SendToServerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Request;

public class ReportUtil {
    public static String TAG = "ReportUtil";

    public void reportEvent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取今天的记录
                Calendar cal = Calendar.getInstance();
                final String nowMin = new SimpleDateFormat("yyyyMMddhhmm").format(cal.getTime());

                //删除一个月之前的数据
                cal.add(Calendar.MONTH, -1);//得到前一个月
                final String lastOneMouth = new SimpleDateFormat("yyyyMMddhhmm").format(cal.getTime());
                //RedHotReportRequestManager.getInstance().delOneMouthAgo(lastOneMouth);

                ZLog.e(TAG, "repHotReports nowMin:" + nowMin);
                final List<RepHotReport> repHotReports = RedHotReportRequestManager.getInstance().queryByNotMin(nowMin);
                ZLog.e(TAG, "repHotReports repHotReports:" + repHotReports);
                if (repHotReports != null && repHotReports.size() > 0) {
                    ZLog.e(TAG, "repHotReports:" + repHotReports);
                    SendToServerUtil.sendRepHotareaToServer(repHotReports, new SendToServerUtil.MyYewuResponseHandle() {
                        @Override
                        public void onSuccess(MyApiResponse response) {
                            ZLog.e(TAG, "sendRepHotareaToServer onSucess:" + response.toString());
                            if (response.code.equals("0")) {
                                RedHotReportRequestManager.getInstance().delList(repHotReports);
                            }
                        }

                        @Override
                        public void onFailure(Request request, Exception e) {
                            ZLog.e(TAG, "sendRepHotareaToServer failure:" + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    public void reportScence() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //删除一个月之前的数据
                //获取除了今天的记录
                Calendar cal = Calendar.getInstance();
                final String today = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

                //删除一个月之前的数据
                cal.add(Calendar.MONTH, -1);//得到前一个月
                final String lastOneMouth = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

                //ScenceReportRequestManager.getInstance().delOneMouthAgo(lastOneMouth);

                ZLog.e(TAG, "scenceReports:" + today);
                final List<ScenceReport> scenceReports = ScenceReportRequestManager.getInstance().queryByNotToday(today);
                if (scenceReports != null && scenceReports.size() > 0) {
                    ZLog.e(TAG, "repHotReports:" + scenceReports);
                    SendToServerUtil.sendScenctToServer(scenceReports, new SendToServerUtil.MyYewuResponseHandle() {
                        @Override
                        public void onSuccess(MyApiResponse response) {
                            ZLog.e(TAG, "sendScenctToServer onSuccess:" + response.toString());
                            if (response.code.equals("0")) {

                                List<ScenceReport> scenceReports1 = new ArrayList<>();

                                for (int i = 0; i < scenceReports.size(); i++) {
                                    if (!scenceReports.get(i).getPalyDate().equals(today)) {
                                          scenceReports1.add(scenceReports.get(i));
                                    }
                                }

                                ScenceReportRequestManager.getInstance().delList(scenceReports1);

                            }
                        }

                        @Override
                        public void onFailure(Request request, Exception e) {
                            ZLog.e(TAG, "sendScenctToServer onFailure:" + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }


    public void reportTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取除了今天的记录
                Calendar cal = Calendar.getInstance();
                String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                ZLog.e(TAG, "scenceReports:" + today);
                List<ScenceReport> scenceReports = ScenceReportRequestManager.getInstance().queryByNotToday(today);
                if (scenceReports != null && scenceReports.size() > 0) {
                    ZLog.e(TAG, "scenceReports:" + scenceReports);
                    SendToServerUtil.sendScenctToServer(scenceReports, new SendToServerUtil.MyYewuResponseHandle() {
                        @Override
                        public void onSuccess(MyApiResponse response) {

                        }

                        @Override
                        public void onFailure(Request request, Exception e) {

                        }
                    });
                    ScenceReportRequestManager.getInstance().delByNotToday(today);
                }/* else {
                        ZLog.e(TAG, "scenceReports:" + null);
                        scenceReports = new ArrayList<>();
                        ScenceReport scenceReport = new ScenceReport();
                        scenceReport.setPalyDate("2020-12-12");
                        scenceReport.setPalySecond(123);
                        scenceReport.setPalyNum(1);
                        scenceReport.setProgramId("123");
                        scenceReport.setTerminalIdentity(DeviceUtil.getTerDeviceID(AppContext.getInstance()));
                        scenceReport.setTerminalName(DeviceUtil.getTerDeviceID(AppContext.getInstance()));
                        scenceReport.setProgramName("ProgramName");
                        scenceReport.setSceneName("ProgramName");
                        scenceReport.setSceneId(12);
                        scenceReports.add(scenceReport);
                        ScenceReport scenceReport1 = new ScenceReport();
                        scenceReport1.setPalyDate("2020-12-12");
                        scenceReport1.setPalySecond(123);
                        scenceReport1.setPalyNum(1);
                        scenceReport1.setProgramId("123");
                        scenceReport1.setTerminalIdentity(DeviceUtil.getTerDeviceID(AppContext.getInstance()));
                        scenceReport1.setTerminalName(DeviceUtil.getTerDeviceID(AppContext.getInstance()));
                        scenceReport1.setProgramName("ProgramName");
                        scenceReport1.setSceneName("ProgramName");
                        scenceReport1.setSceneId(12);
                        scenceReports.add(scenceReport1);
                        ZLog.e(TAG, "scenceReports:" + scenceReports);
                        SendToServerUtil.sendScenctToServer(scenceReports);
                        ScenceReportRequestManager.getInstance().delByNotToday(today);

                    }*/

                List<RepHotReport> repHotReports = RedHotReportRequestManager.getInstance().queryByNotMin(today);
                if (repHotReports != null && repHotReports.size() > 0) {
                    ZLog.e(TAG, "repHotReports:" + repHotReports);
                    SendToServerUtil.sendRepHotareaToServer(repHotReports, new SendToServerUtil.MyYewuResponseHandle() {
                        @Override
                        public void onSuccess(MyApiResponse response) {

                        }

                        @Override
                        public void onFailure(Request request, Exception e) {

                        }
                    });
                    RedHotReportRequestManager.getInstance().delByNotToday(today);
                } /*else {
                        ZLog.e(TAG, "repHotReports:" + null);
                        repHotReports = new ArrayList<>();
                        RepHotReport repHotReport = new RepHotReport();
                        repHotReport.setStartTime("2020-12-12");
                        repHotReport.setClickNum(1);
                        repHotReport.setTerminalIdentity(DeviceUtil.getTerDeviceID(AppContext.getInstance()));
                        repHotReport.setTerminalName(DeviceUtil.getTerDeviceID(AppContext.getInstance()));
                        repHotReport.setProgramName("ProgramName");
                        repHotReport.setSceneName("SceneName");
                        repHotReport.setSceneId(12312);
                        repHotReports.add(repHotReport);

                        RepHotReport repHotReport1 = new RepHotReport();
                        repHotReport1.setStartTime("2020-12-12");
                        repHotReport1.setClickNum(1);
                        repHotReport1.setTerminalIdentity(DeviceUtil.getTerDeviceID(AppContext.getInstance()));
                        repHotReport1.setTerminalName(DeviceUtil.getTerDeviceID(AppContext.getInstance()));
                        repHotReport1.setProgramName("ProgramName");
                        repHotReport1.setSceneName("SceneName");
                        repHotReport1.setSceneId(12312);
                        repHotReports.add(repHotReport1);

                        ZLog.e(TAG, "repHotReports:" + repHotReports);
                        SendToServerUtil.sendRepHotareaToServer(repHotReports);
                        RedHotReportRequestManager.getInstance().delByNotToday(today);
                    }*/
            }
        }).start();
    }


    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        final String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        //删除一个月之前的数据
        cal.add(Calendar.MONTH, -1);//得到前一个月
        final String lastOneMouth = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

        System.out.println("" + today + "::" + lastOneMouth);
    }
}
