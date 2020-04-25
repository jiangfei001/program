package com.sgs.businessmodule.websocketmodel.js;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.RedHotReportRequestManager;
import com.sgs.businessmodule.upReportModel.RepHotReport;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgarmPalySceneVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class JsInterface {

    private static final String TAG = "JsInterface";
    private Context mContext;

    public JsInterface(Context context) {
        this.mContext = context;
    }


    public void saveRepHotReport(String areaName, String pageName) {
        String nowDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date());

        List<ProgarmPalySceneVo> progarmPalySceneVos = ProgramScheduledManager.getInstance().programTaskManager.nowProgarmPalySceneVos;
        if (progarmPalySceneVos == null) {
            Log.e(TAG, "progarmPalySceneVos==null");
            return;
        }
        ProgarmPalyInstructionVo nowProgarmPalyInstructionVo = ProgramScheduledManager.getInstance().programTaskManager.nowProgarmPalyInstructionVo;
        if (nowProgarmPalyInstructionVo == null) {
            Log.e(TAG, "nowProgarmPalyInstructionVo==null");
            return;
        }
        int nowscene = ProgramScheduledManager.getInstance().programTaskManager.nowscene;

        RepHotReport repHotReport = RedHotReportRequestManager.getInstance().queryByDateAndScenceId(progarmPalySceneVos.get(nowscene).getSceneId(), nowDate, areaName, pageName);
        Log.e(TAG, "sendPlayHtml:repHotReport:" + repHotReport);
        if (repHotReport == null) {
            repHotReport = new RepHotReport();
            repHotReport.setStartTime(nowDate);
            repHotReport.setClickNum(1);
            repHotReport.setTerminalIdentity(DeviceUtil.getUniqueID(AppContext.getInstance()));
            repHotReport.setTerminalName(DeviceUtil.getUniqueID(AppContext.getInstance()));
            repHotReport.setProgramName(nowProgarmPalyInstructionVo.getProgramName());
            repHotReport.setSceneName(progarmPalySceneVos.get(nowscene).getSceneName());
            repHotReport.setSceneId(progarmPalySceneVos.get(nowscene).getSceneId());
            Log.e(TAG, "repHotReport:" + repHotReport.toString());
        } else {
            repHotReport.setClickNum(repHotReport.getClickNum() + 1);
            repHotReport.setEndTime(nowDate);
            Log.e(TAG, "repHotReport:" + repHotReport.toString());
        }
        RedHotReportRequestManager.getInstance().saveInstructionRequest(repHotReport);
    }


    //在js中调用window.test.showInfoFromJs(name)，便会触发此方法。
    @JavascriptInterface
    public void updateClickEventFromJs(String eventName, String pageName) {
        Log.e("aa", "pageName" + pageName + "eventName" + eventName);
        //记录埋点信息 日期
        Toast.makeText(mContext, eventName, Toast.LENGTH_SHORT).show();
        saveRepHotReport(eventName, pageName);
    }

    @JavascriptInterface
    public void openApp(String action, String packagename) {
        Log.e("aa", "action" + action + "packagename" + packagename);
        Intent intent = new Intent();
        intent.setAction(action);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage(packagename);
        this.mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void openAppByActivy(String activity, String packagename) {
        Log.e("aa", "activity" + activity + "packagename" + packagename);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        /**知道要跳转应用的包命与目标Activity*/
        ComponentName componentName = new ComponentName(packagename, activity);
        intent.setComponent(componentName);
        this.mContext.startActivity(intent);
    }

}
