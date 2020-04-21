package com.sgs.businessmodule.websocketmodel.js;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.j256.ormlite.field.DatabaseField;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.RedHotReportRequestManager;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.ScenceReportRequestManager;
import com.sgs.businessmodule.upReportModel.RepHotReport;
import com.sgs.businessmodule.upReportModel.ScenceReport;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.programModel.ProgramScheduledManager;
import com.sgs.programModel.ProgramTaskManager;
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


    private void saveRepHotReport(String eventArea) {
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        List<ProgarmPalySceneVo> progarmPalySceneVos = ProgramScheduledManager.getInstance().programTaskManager.nowProgarmPalySceneVos;
        ProgarmPalyInstructionVo nowProgarmPalyInstructionVo = ProgramScheduledManager.getInstance().programTaskManager.nowProgarmPalyInstructionVo;
        int nowscene = ProgramScheduledManager.getInstance().programTaskManager.nowscene;

        RepHotReport repHotReport = RedHotReportRequestManager.getInstance().queryByDateAndScenceId(progarmPalySceneVos.get(nowscene).getSceneId(), nowDate, eventArea);
        Log.e(TAG, "sendPlayHtml:repHotReport:" + repHotReport);

        if (repHotReport == null) {
            repHotReport = new RepHotReport();
            repHotReport.setStartTime(nowDate);
            repHotReport.setClickNum(1);
            repHotReport.setTerminalIdentity(DeviceUtil.getUniqueID(this.mContext));
            repHotReport.setTerminalName(DeviceUtil.getUniqueID(this.mContext));
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
    public void updateClickEventFromJs(String eventName, String eventJson) {
        Log.e("aa", "eventJson" + eventJson + "eventName" + eventName);
        //记录埋点信息 日期


        Toast.makeText(mContext, eventName, Toast.LENGTH_SHORT).show();
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
