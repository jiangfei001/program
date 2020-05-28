package com.uiModel.activity;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zhangke.zlog.ZLog;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sgs.AppContext;
import com.sgs.AppUrl;
import com.sgs.businessmodule.downloadModel.dbcontrol.FileHelper;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.MyApiResponse;
import com.sgs.businessmodule.httpModel.MyHttpResponseHandler;
import com.sgs.businessmodule.websocketmodel.WebSocketActivityRelease;
import com.sgs.middle.utils.DeviceUtil;
import com.jf.fine.R;
import com.sgs.middle.utils.StringUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

public class LoginActivity extends BaseActivity {

    Handler handler = new Handler();

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE};

    List<String> mPermissionList = new ArrayList<>();

    private static final int PERMISSION_REQUEST = 1;

    private AlertDialog alertDialog;

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText editText = findViewById(R.id.code);
        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editText.setText(DeviceUtil.getTerDeviceID(LoginActivity.this));

        final EditText yonghuming = findViewById(R.id.yonghuming);
        final EditText shebeiName = findViewById(R.id.shebeiName);
        final EditText socketip = findViewById(R.id.socketip);
        final EditText jiekouip = findViewById(R.id.jiekouip);


        final RadioGroup radioButton = (RadioGroup) findViewById(R.id.radiogroup1);


        if (true) {
            radioButton.check(R.id.bujia);
            socketip.setText(AppUrl.socketIP);
            jiekouip.setText(AppUrl.jiekouIP);

        } else {
            radioButton.check(R.id.jia);
            socketip.setText(AppUrl.socketIPTest);
            jiekouip.setText(AppUrl.jiekouIPTest);
        }

        shebeiName.setText(DeviceUtil.getSBM(this));
        radioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioButton.getCheckedRadioButtonId();
                if (id == R.id.jia) {
                    socketip.setText(AppUrl.socketIPTest);
                    jiekouip.setText(AppUrl.jiekouIPTest);
                } else {
                    socketip.setText(AppUrl.socketIP);
                    jiekouip.setText(AppUrl.jiekouIP);
                }
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.showLoadingDialog();
                boolean isjihuo = false;

                if (!StringUtil.isEmpty(FileHelper.getSDunique(FileHelper.isjihuo))) {
                    ZLog.e("isjihuo", "is" + FileHelper.getSDunique(FileHelper.isjihuo));
                    isjihuo = true;
                }

                if (!isjihuo) {
                    LoginActivity.this.dismissLoadingDialog();
                    Toast.makeText(LoginActivity.this, "请先激活！", Toast.LENGTH_LONG).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        zhuce(radioButton, socketip, jiekouip, yonghuming, shebeiName);
                    }
                }).start();
            }
        });

        findViewById(R.id.btnSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.showLoadingDialog();
                boolean s_test = false;
                int id = radioButton.getCheckedRadioButtonId();
                if (id == R.id.jia) {
                    s_test = true;
                } else {
                    s_test = false;
                }
                AppUrl.initip(s_test);

                android.content.SharedPreferences mContextSp = AppContext.getInstance().getSharedPreferences(DeviceUtil.sfter, Context.MODE_PRIVATE);
                /*boolean iszhuce = mContextSp.getBoolean(DeviceUtil.iszhuce, false);
                boolean isjihuo = mContextSp.getBoolean(DeviceUtil.isjihuo, false);*/
                boolean iszhuce = false;
                boolean isjihuo = false;

                if (!StringUtil.isEmpty(FileHelper.getSDunique(FileHelper.iszhuce))) {
                    ZLog.e("iszhuce", "is" + FileHelper.getSDunique(FileHelper.iszhuce));
                    iszhuce = true;
                }

                if (!StringUtil.isEmpty(FileHelper.getSDunique(FileHelper.isjihuo))) {
                    ZLog.e("isjihuo", "is" + FileHelper.getSDunique(FileHelper.isjihuo));
                    isjihuo = true;
                }

                /*boolean iszhuce = mContextSp.getBoolean(DeviceUtil.iszhuce, false);
                boolean isjihuo = mContextSp.getBoolean(DeviceUtil.isjihuo, false);*/

                if (!isjihuo) {
                    LoginActivity.this.dismissLoadingDialog();
                    Toast.makeText(LoginActivity.this, "请先激活！", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!iszhuce) {
                    LoginActivity.this.dismissLoadingDialog();
                    Toast.makeText(LoginActivity.this, "请先注册！", Toast.LENGTH_LONG).show();
                    return;
                }


                AppContext.getInstance().userName = yonghuming.getText().toString();
                getIp();
              /*  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        zhuce(radioButton, socketip, jiekouip, yonghuming, shebeiName);
                    }
                }).start();*/

            }
        });
        findViewById(R.id.jihuo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.showLoadingDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jihuo(radioButton, socketip, jiekouip, yonghuming, shebeiName);
                    }
                }).start();

            }
        });

        initPermission();


        /*NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }*/
    }

    private void jihuo(RadioGroup radioButton, EditText socketip, EditText jiekouip,
                       final EditText yonghuming, final EditText shebeiName) {
        boolean s_test = false;
        int id = radioButton.getCheckedRadioButtonId();
        if (id == R.id.jia) {
            s_test = true;
        } else {
            s_test = false;
        }
        //socketip.getText().toString().trim(), jiekouip.getText().toString().trim(),
        AppUrl.initip(s_test);

        final HashMap hashMap = new HashMap();

        hashMap.put("secretKey", DeviceUtil.getSercetKey(LoginActivity.this));
        hashMap.put("terminalIdentity", DeviceUtil.getTerDeviceID(LoginActivity.this));

        ZLog.e("HashMap", hashMap.toString());

        HttpClient.postHashMapEntity(AppUrl.activation, hashMap, new
                MyHttpResponseHandler() {
                    @Override
                    public void onSuccess(final MyApiResponse response) {
                        ZLog.e("tag", "response.msg ");
                        final Handler handler1 = new Handler(Looper.getMainLooper());
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.this.dismissLoadingDialog();
                                if (response.code.equals("0")) {
                                    Toast.makeText(AppContext.getInstance(), "恭喜你激活成功了啊！！", Toast.LENGTH_LONG).show();
                                    FileHelper.putSDunique("isjihuo", FileHelper.isjihuo);
                                } else {
                                    if (response.code.equals("1")) {
                                        Toast.makeText(AppContext.getInstance(), response.msg + "|" + response.code, Toast.LENGTH_LONG).show();
                                        FileHelper.putSDunique("isjihuo", FileHelper.isjihuo);
                                    } else {
                                        ZLog.e("tag", "response.msg ");
                                        Toast.makeText(AppContext.getInstance(), response.msg + "|" + response.code, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        e.printStackTrace();
                        ZLog.e("tag", "onFailure.msg ");
                        Handler handler1 = new Handler(Looper.getMainLooper());
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.this.dismissLoadingDialog();
                                Toast.makeText(LoginActivity.this, "对不起，激活失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }

    private void zhuce(RadioGroup radioButton, final EditText socketip, final EditText jiekouip,
                       final EditText yonghuming, final EditText shebeiName) {
        boolean s_test = false;
        int id = radioButton.getCheckedRadioButtonId();
        if (id == R.id.jia) {
            s_test = true;
        } else {
            s_test = false;
        }
        //socketip.getText().toString().trim(), jiekouip.getText().toString().trim(),
        AppUrl.initip(s_test);

        final HashMap hashMap = new HashMap();

        hashMap.put("userName", yonghuming.getText().toString());

        hashMap.put("terminalIdentity", DeviceUtil.getTerDeviceID(LoginActivity.this));

        hashMap.put("terminalName", !StringUtil.isEmpty(shebeiName.getText().toString()) ? shebeiName.getText().toString() : DeviceUtil.getTerDeviceID(LoginActivity.this));
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
        hashMap.put("equipmentNo", DeviceUtil.getTerDeviceID(LoginActivity.this));
        //设备序列号
        hashMap.put("equipmentSerial", DeviceUtil.getMobileSerial(LoginActivity.this));
        //磁盘物理路径
        hashMap.put("physicalPath", DeviceUtil.getDir());
        //磁盘大小
        hashMap.put("diskSize", DeviceUtil.getDeviceTotalRam());
        //磁盘剩余大小
        hashMap.put("diskRest", DeviceUtil.getDeviceRemainRam());
        //最近连接时间
        hashMap.put("recentConnectTime", "");
        //地址
        hashMap.put("address", AppContext.getInstance().addr);

        hashMap.put("gatewayIp", DeviceUtil.getNetIp());

        ZLog.e("HashMap", hashMap.toString());

        HttpClient.postHashMapEntity(AppUrl.serverUrlAddMuTerminal, hashMap, new
                MyHttpResponseHandler() {
                    @Override
                    public void onSuccess(final MyApiResponse response) {
                        ZLog.e("tag", "response.msg ");
                        final Handler handler1 = new Handler(Looper.getMainLooper());
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.this.dismissLoadingDialog();
                                if (response.code.equals("0")) {
                                    Toast.makeText(AppContext.getInstance(), "恭喜你注册成功了啊！！", Toast.LENGTH_LONG).show();
                                    AppContext.getInstance().userName = yonghuming.getText().toString();
                                    //保存sp
                                    android.content.SharedPreferences mContextSp = AppContext.getInstance().getSharedPreferences(DeviceUtil.sfter, Context.MODE_PRIVATE);
                                    android.content.SharedPreferences.Editor editor = mContextSp.edit();
                                    editor.putString(DeviceUtil.sbm, shebeiName.getText().toString());
                                    editor.commit();
                                    FileHelper.putSDunique("iszhuce", FileHelper.iszhuce);
                                    //getIp();
                                } else {
                                    //注册接口 1设备已经存在   2 用户不存在  3 参数不能为空
                                    if (response.code.equals("1")) {
                                        Toast.makeText(AppContext.getInstance(), response.msg + "|" + response.code, Toast.LENGTH_LONG).show();
                                        AppContext.getInstance().userName = yonghuming.getText().toString();
                                        android.content.SharedPreferences mContextSp = AppContext.getInstance().getSharedPreferences(DeviceUtil.sfter, Context.MODE_PRIVATE);
                                        android.content.SharedPreferences.Editor editor = mContextSp.edit();
                                        editor.putString(DeviceUtil.sbm, shebeiName.getText().toString());
                                        editor.commit();
                                        FileHelper.putSDunique("iszhuce", FileHelper.iszhuce);
                                    } else {
                                        Toast.makeText(AppContext.getInstance(), response.msg + "|" + response.code, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        e.printStackTrace();
                        ZLog.e("tag", "onFailure.msg ");
                        Handler handler1 = new Handler(Looper.getMainLooper());
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.this.dismissLoadingDialog();
                                Toast.makeText(LoginActivity.this, "对不起，注册失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }

    private void getIp() {
        //获取ip
        HttpClient.postHashMapEntity(AppUrl.getServerList, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(final MyApiResponse response) {
                super.onSuccess(response);
                ZLog.e("HashMap", response.toString());
                if (response.code.equals("0")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            LoginActivity.this.dismissLoadingDialog();
                            AppUrl.setSerList(response.getData());
                            doNavigation();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            LoginActivity.this.dismissLoadingDialog();
                            if (response.code.equals("1")) {
                                Toast.makeText(AppContext.getInstance(), "请先激活或者注册！", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AppContext.getInstance(), "请先激活或者注册！", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
/*                {
    "code": 0,
    "data": [
        "49.235.109.237:9080/multimedia",
        "49.235.109.237:8081/multimedia_test"
    ]
                }*/

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                Toast.makeText(AppContext.getInstance(), "获取ip失败激活！" + e.getMessage(), Toast.LENGTH_LONG).show();
                ZLog.e("HashMap", "" + e.getMessage());
            }
        });
    }

    private void doNavigation() {
        Intent it = new Intent(this, WebSocketActivityRelease.class);
        startActivity(it);
        finish();
    }

    private void initPermission() {
        mPermissionList.clear();
        /**
         * 判断哪些权限未授予
         */
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(LoginActivity.this, permissions, PERMISSION_REQUEST);
        }
    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ZLog.e("DMP", "DMP" + DeviceUtil.getDisplayMetricsPixels(this));
        MobclickAgent.onResume(this);
       /* if (SharedPreferences.getInstance().getBoolean(SharedPreferences.KEY_ISREGISTER, false)) {
            doNavigation();
        }*/
    }

    private void initView() {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
