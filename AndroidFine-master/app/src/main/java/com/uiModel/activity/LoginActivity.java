package com.uiModel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sgs.AppContext;
import com.sgs.AppUrl;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.MyApiResponse;
import com.sgs.businessmodule.httpModel.MyHttpResponseHandler;
import com.sgs.businessmodule.websocketmodel.WebSocketActivity;
import com.sgs.middle.utils.DeviceUtil;
import com.jf.fine.R;

import java.util.HashMap;

import okhttp3.Request;

public class LoginActivity extends Activity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText editText = findViewById(R.id.code);
        editText.setText(DeviceUtil.getUniqueID(LoginActivity.this));

        final EditText phone = findViewById(R.id.phone);
        phone.setText("admin");

        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });


        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final HashMap hashMap = new HashMap();

                        hashMap.put("userName", phone.getText().toString());

                        hashMap.put("terminalIdentity", DeviceUtil.getUniqueID(LoginActivity.this));

                        hashMap.put("terminalName", DeviceUtil.getUniqueID(LoginActivity.this));
                        //应用版本号
                        hashMap.put("appVersion", DeviceUtil.getVersionName(LoginActivity.this));
                        //局域网IP地址
                        hashMap.put("lanIp", DeviceUtil.getIPAddress(LoginActivity.this));
        /*        //网关IP地址/
                hashMap.put("gatewayIp", DeviceUtil.getNetIp());*/
                        //mac地址
                        hashMap.put("mac", DeviceUtil.getWifiMacAddress(LoginActivity.this));
                        //分辨率
                        hashMap.put("resolution", DeviceUtil.getDisplayMetricsPixels(LoginActivity.this));
                        //固件信息
                        hashMap.put("firmwareInfo", DeviceUtil.getPhoneBrand() + "BuildLevel" + DeviceUtil.getBuildLevel());
                        //CPU ID
                        hashMap.put("cpuId", DeviceUtil.getCPU());
                        //系统编号
                        hashMap.put("systemNo", DeviceUtil.getBuildVersion());
                        //设备身份编码
                        hashMap.put("equipmentNo", DeviceUtil.getUniqueID(LoginActivity.this));
                        //设备序列号
                        hashMap.put("equipmentSerial", DeviceUtil.getMobileSerial(LoginActivity.this));
                        //磁盘物理路径
                        hashMap.put("physicalPath", DeviceUtil.getDir());
                        //磁盘大小
                        hashMap.put("diskSize", DeviceUtil.getDeviceTotalRam());
                        //磁盘剩余大小
                        hashMap.put("diskRest", DeviceUtil.getDeviceRemainRam());
                        //最近连接时间
                        hashMap.put("recentConnectTime", DeviceUtil.getConnectionTime1());
                        //地址
                        hashMap.put("address", AppContext.getInstance().addr);

                        hashMap.put("gatewayIp", DeviceUtil.getNetIp());

                        Log.e("HashMap", hashMap.toString());

                        HttpClient.postHashMapEntity(AppUrl.serverUrl, hashMap, new
                                MyHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(final MyApiResponse response) {
                                        Log.e("tag", "response.msg ");
                                        Handler handler1 = new Handler(Looper.getMainLooper());
                                        handler1.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(AppContext.getInstance(), response.msg + "|" + response.code, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Request request, Exception e) {
                                        e.printStackTrace();
                                        Log.e("tag", "onFailure.msg ");
                                        Handler handler1 = new Handler(Looper.getMainLooper());
                                        handler1.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this, "对不起，注册失败", Toast.LENGTH_LONG).show();
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
                AppContext.getInstance().userName = phone.getText().toString();
                doNavigation();
            }
        });
    }


    private void doNavigation() {
        Intent it = new Intent(this, WebSocketActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
