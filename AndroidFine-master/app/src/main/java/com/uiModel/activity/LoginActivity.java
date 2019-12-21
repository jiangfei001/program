package com.uiModel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
                
                hashMap.put("userName", DeviceUtil.getPhoneSign(LoginActivity.this));

                hashMap.put("terminalIdentity", DeviceUtil.getPhoneSign(LoginActivity.this));

                hashMap.put("terminalName", DeviceUtil.getPhoneSign(LoginActivity.this));
                //应用版本号
                hashMap.put("appVersion", DeviceUtil.getVersionName(LoginActivity.this));
                //局域网IP地址
                hashMap.put("lanIp", DeviceUtil.getIPAddress(LoginActivity.this));
                //网关IP地址
                hashMap.put("gatewayIp", DeviceUtil.getNetIp());
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
                hashMap.put("equipmentNo", DeviceUtil.getPhoneSign(LoginActivity.this));
                //设备序列号
                hashMap.put("equipmentSerial", DeviceUtil.getMobileSerial());
                //磁盘物理路径
                hashMap.put("physicalPath", DeviceUtil.getDir());
                //磁盘大小
                hashMap.put("diskSize", DeviceUtil.getDeviceTotalRam());
                //磁盘剩余大小
                hashMap.put("diskRest", DeviceUtil.getDeviceRemainRam());
                //最近连接时间
                hashMap.put("recentConnectTime", DeviceUtil.getConnectionTime());
                //地址
                hashMap.put("address", "456");

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
                                e.printStackTrace();
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

    public String serverUrl = "http://49.235.109.237:9080/multimedia/api/terminal/addMuTerminal";

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
