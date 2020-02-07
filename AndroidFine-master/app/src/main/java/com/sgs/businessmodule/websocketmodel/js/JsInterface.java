package com.sgs.businessmodule.websocketmodel.js;

import android.content.Context;
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
        Toast.makeText(mContext, eventName, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void getInfoFromJs(int a, int b) {
        int c = a + b;
        Toast.makeText(mContext, "" + c, Toast.LENGTH_SHORT).show();
    }
}
