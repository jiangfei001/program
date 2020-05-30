package com.sgs.businessmodule.websocketmodel;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sgs.AppContext;
import com.sgs.ReportUtil;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventManager;
import com.jf.fine.R;
import com.sgs.middle.receiver.CustomAlarmReceiver;
import com.sgs.middle.utils.UsageStatsManagerUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Method;

public abstract class EventActivity extends AppCompatActivity {

    private void initView() {
        /*//设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initView();
        super.onCreate(savedInstanceState);
        AppContext.getInstance().setNowActivity(this);

        UsageStatsManagerUtil.getInstance().alarmUploadDataOnceDaily();
        UsageStatsManagerUtil.getInstance().alarmSendHotAreaReportUsage();
        UsageStatsManagerUtil.getInstance().alarmSendScence();

        CustomAlarmReceiver.cvds();
        CustomAlarmReceiver.setco();

        ReportUtil reportUtil = new ReportUtil();
        reportUtil.reportEvent();
        reportUtil.reportScence();

    }

    protected void webViewInit(WebView webView1) {
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setUseWideViewPort(true);
        webView1.getSettings().setLoadWithOverviewMode(true);
        webView1.getSettings().setAllowFileAccess(true);
        webView1.getSettings().setSupportZoom(true);
        webView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webView1.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webView1.getSettings(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        webView1.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView1.getSettings().setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK
        /* wvBookPlay.setWebChromeClient(new MyWebChromeClient());// 重写一下，有的时候可能会出现问题*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView1.getSettings().setMixedContentMode(webView1.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventManager.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.unregister(this);
    }

    @Subscribe
    public void onEvent(Event mEvent) {
    }
}
