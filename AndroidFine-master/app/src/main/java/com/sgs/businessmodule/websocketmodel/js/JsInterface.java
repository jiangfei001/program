package com.sgs.businessmodule.websocketmodel.js;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


public class JsInterface {
    private Context mContext;

    public JsInterface(Context context) {
        this.mContext = context;
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
