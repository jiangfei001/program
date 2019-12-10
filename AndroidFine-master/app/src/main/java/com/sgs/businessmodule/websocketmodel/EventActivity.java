package com.sgs.businessmodule.websocketmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sgs.AppContext;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventManager;

import org.greenrobot.eventbus.Subscribe;

public abstract class EventActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
