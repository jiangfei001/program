package com.uiModel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.HttpResponseHandler;
import com.sgs.businessmodule.httpModel.RestApiResponse;
import com.sgs.businessmodule.websocketmodel.InstructionResponse;
import com.sgs.businessmodule.websocketmodel.WebSocketActivity;
import com.sgs.middle.utils.DeviceUtil;
import com.yuzhi.fine.R;

import java.util.HashMap;

import okhttp3.Request;

public class LoginActivity extends Activity {

    Handler handler = new Handler();

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


        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final HashMap hashMap = new HashMap();
                hashMap.put("userName", "admin");
                hashMap.put("terminalIdentity", "123");
                hashMap.put("terminalName", "456");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient.postHashMapEntity(serverUrl, hashMap, new HttpResponseHandler() {
                            @Override
                            public void onSuccess(RestApiResponse response) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "恭喜注册成功", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Request request, Exception e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "恭喜注册失败", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }
                }).start();
            }
        });
        findViewById(R.id.btnSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNavigation();
            }
        });
    }

    public String serverUrl = "http://192.168.0.109:8081/multimedia/api/terminal/addMuTerminal";

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
