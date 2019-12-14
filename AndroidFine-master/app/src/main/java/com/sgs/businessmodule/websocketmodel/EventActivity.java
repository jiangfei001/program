package com.sgs.businessmodule.websocketmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.sgs.AppContext;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventManager;

import org.greenrobot.eventbus.Subscribe;

public abstract class EventActivity extends AppCompatActivity {

    private void initView() {
        /*//设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initView();
        super.onCreate(savedInstanceState);
        AppContext.getInstance().setNowActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
