package com.uiModel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sgs.businessmodule.websocketmodel.WebSocketActivity;
import com.yuzhi.fine.R;


/**
 * Created by tiansj on 15/7/31.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNavigation();
            }
        });
        findViewById(R.id.btnSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNavigation();
            }
        });
    }

    private void doNavigation() {
        Intent it = new Intent(this, WebSocketActivity.class);
        startActivity(it);
        finish();
    }

    private void initView() {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
    }

}
