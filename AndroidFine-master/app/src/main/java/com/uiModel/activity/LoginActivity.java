package com.uiModel.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.utils.Sha256Hash;
import com.uiModel.Jihuo;
import com.uiModel.loginUtil.LoginUtil;
import com.zhangke.zlog.ZLog;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sgs.AppContext;
import com.sgs.AppUrl;
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
import java.util.Map;

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
                boolean isjihuo = LoginUtil.isjihuo();
                if (!StringUtil.isEmpty(LoginUtil.getTerminalIdentity())) {
                    ZLog.e("isjihuo", "isjihuo");
                    isjihuo = true;
                }
                if (!isjihuo) {
                    LoginActivity.this.dismissLoadingDialog();
                    Toast.makeText(LoginActivity.this, "请先激活！", Toast.LENGTH_LONG).show();
                    return;
                }

                LoginActivity.this.showLoadingDialog("注册中..");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        zhuce(radioButton, yonghuming, shebeiName);
                    }
                }).start();
            }
        });

        findViewById(R.id.btnSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean s_test = false;
                int id = radioButton.getCheckedRadioButtonId();
                if (id == R.id.jia) {
                    s_test = true;
                } else {
                    s_test = false;
                }
                AppUrl.initip(s_test);

                boolean iszhuce = LoginUtil.getIsZhuche();
                boolean isjihuo = LoginUtil.isjihuo();

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
                checkRegisterBinding();
            }
        });
        findViewById(R.id.jihuo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jihuo(radioButton);
            }
        });

        initPermission();


        AutoLogin(yonghuming, shebeiName, radioButton);




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

    private void AutoLogin(final EditText yonghuming, final EditText shebeiName, final RadioGroup radioButton) {
        boolean isjihuo = LoginUtil.isjihuo();
        if (!StringUtil.isEmpty(LoginUtil.getTerminalIdentity())) {
            ZLog.e("isjihuo", "isjihuo");
            isjihuo = true;
        }
        boolean s_test = false;
        int id = radioButton.getCheckedRadioButtonId();
        if (id == R.id.jia) {
            s_test = true;
        } else {
            s_test = false;
        }
        AppUrl.initip(s_test);
        if (!isjihuo) {
            final HashMap hashMap = new HashMap();
            hashMap.put("terminalIdentity", DeviceUtil.getTerDeviceID(AppContext.getInstance()));
            String timeStamp = String.valueOf(System.currentTimeMillis());
            hashMap.put("token", Sha256Hash.getToken(DeviceUtil.getTerDeviceID(AppContext.getInstance()), timeStamp, Sha256Hash.key));
            hashMap.put("timeStamp", timeStamp);
            ZLog.e("HashMap", hashMap.toString());
            LoginActivity.this.showLoadingDialog("激活中..");
            HttpClient.postHashMapEntity(AppUrl.activation, hashMap, new
                    MyHttpResponseHandler() {
                        @Override
                        public void onSuccess(final MyApiResponse response) {
                            ZLog.e("tag", "response.msg ");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    LoginActivity.this.dismissLoadingDialog();
                                    if (response.code.equals("0")) {
                                        String jihuoJson = response.getData();
                                        ZLog.e("tag", "jihuoJson" + jihuoJson);
                                        Jihuo jihuo = JSONObject.parseObject(jihuoJson, Jihuo.class);
                                        if (jihuo == null || TextUtils.isEmpty(jihuo.getTerminalIdentity()) || TextUtils.isEmpty(jihuo.getSecretKey())) {
                                            Toast.makeText(AppContext.getInstance(), "您激活失败了啊！！" + jihuoJson, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(AppContext.getInstance(), "恭喜您激活成功了啊！！", Toast.LENGTH_LONG).show();
                                            LoginUtil.putTerminalIdenAndSecretKey(jihuo.getTerminalIdentity(), jihuo.getSecretKey());

                                            LoginActivity.this.showLoadingDialog("注册中..");
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    zhuce(radioButton, yonghuming, shebeiName);
                                                }
                                            }).start();
                                        }
                                    } else {
                                        ZLog.e("tag", "response.msg" + response.msg + "|" + response.code);
                                        if (response.code.equals("1")) {
                                            Toast.makeText(AppContext.getInstance(), response.msg + "|" + response.code, Toast.LENGTH_LONG).show();
                                            //FileHelper.putSDunique("isjihuo", FileHelper.isjihuo);
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
        } else {
            boolean iszhuce = LoginUtil.getIsZhuche();
            if (!iszhuce) {
                LoginActivity.this.showLoadingDialog("注册中..");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        zhuce(radioButton, yonghuming, shebeiName);
                    }
                }).start();
            } else {
                checkRegisterBinding();
            }
        }
    }

    private void jihuo(RadioGroup radioButton) {
        boolean s_test = false;
        int id = radioButton.getCheckedRadioButtonId();
        if (id == R.id.jia) {
            s_test = true;
        } else {
            s_test = false;
        }
        AppUrl.initip(s_test);

        boolean isjihuo = LoginUtil.isjihuo();

        if (isjihuo) {
            Toast.makeText(LoginActivity.this, "已经激活，不要再激活！", Toast.LENGTH_LONG).show();
            return;
        }

        final HashMap hashMap = new HashMap();
        hashMap.put("terminalIdentity", DeviceUtil.getTerDeviceID(AppContext.getInstance()));
        String timeStamp = String.valueOf(System.currentTimeMillis());
        hashMap.put("token", Sha256Hash.getToken(DeviceUtil.getTerDeviceID(AppContext.getInstance()), timeStamp, Sha256Hash.key));
        hashMap.put("timeStamp", timeStamp);
        ZLog.e("HashMap", hashMap.toString());
        LoginActivity.this.showLoadingDialog("激活中..");
        HttpClient.postHashMapEntity(AppUrl.activation, hashMap, new
                MyHttpResponseHandler() {
                    @Override
                    public void onSuccess(final MyApiResponse response) {
                        ZLog.e("tag", "response.msg ");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.this.dismissLoadingDialog();
                                if (response.code.equals("0")) {
                                    String jihuoJson = response.getData();
                                    ZLog.e("tag", "jihuoJson" + jihuoJson);
                                    Jihuo jihuo = JSONObject.parseObject(jihuoJson, Jihuo.class);
                                    if (jihuo == null || TextUtils.isEmpty(jihuo.getTerminalIdentity()) || TextUtils.isEmpty(jihuo.getSecretKey())) {
                                        Toast.makeText(AppContext.getInstance(), "您激活失败了啊！！" + jihuoJson, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(AppContext.getInstance(), "恭喜您激活成功了啊！！", Toast.LENGTH_LONG).show();
                                        LoginUtil.putTerminalIdenAndSecretKey(jihuo.getTerminalIdentity(), jihuo.getSecretKey());
                                    }
                                } else {
                                    ZLog.e("tag", "response.msg" + response.msg + "|" + response.code);
                                    if (response.code.equals("1")) {
                                        Toast.makeText(AppContext.getInstance(), response.msg + "|" + response.code, Toast.LENGTH_LONG).show();
                                        //FileHelper.putSDunique("isjihuo", FileHelper.isjihuo);
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

    private void zhuce(RadioGroup radioButton, final EditText yonghuming, final EditText shebeiName) {
        boolean s_test = false;
        int id = radioButton.getCheckedRadioButtonId();
        if (id == R.id.jia) {
            s_test = true;
        } else {
            s_test = false;
        }
        AppUrl.initip(s_test);
        final HashMap hashMap = new HashMap();
        LoginUtil.getAMMap(yonghuming, shebeiName, hashMap, this);
        ZLog.e("HashMap", hashMap.toString());

        HttpClient.postHashMapEntity(AppUrl.serverUrlAddMuTerminal, hashMap, new
                MyHttpResponseHandler() {
                    @Override
                    public void onSuccess(final MyApiResponse response) {
                        ZLog.e("tag", "response.msg ");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.this.dismissLoadingDialog();
                                if (response.code.equals("0")) {
                                    Toast.makeText(AppContext.getInstance(), "恭喜你提交成功，等待注册回调！！", Toast.LENGTH_LONG).show();
                                    AppContext.getInstance().userName = yonghuming.getText().toString();
                                    //保存sp
                                    android.content.SharedPreferences mContextSp = AppContext.getInstance().getSharedPreferences(DeviceUtil.sfter, Context.MODE_PRIVATE);
                                    android.content.SharedPreferences.Editor editor = mContextSp.edit();
                                    editor.putString(DeviceUtil.sbm, shebeiName.getText().toString());
                                    editor.commit();
                                    ZLog.e("tag", "注册提交成功 ");
                                    checkRegisterBinding();
                                } else {
                                    //注册接口 1设备已经存在   2 用户不存在  3 参数不能为空
                                    if (response.code.equals("1")) {
                                        Toast.makeText(AppContext.getInstance(), response.msg + "|" + response.code, Toast.LENGTH_LONG).show();
                                        AppContext.getInstance().userName = yonghuming.getText().toString();
                                        android.content.SharedPreferences mContextSp = AppContext.getInstance().getSharedPreferences(DeviceUtil.sfter, Context.MODE_PRIVATE);
                                        android.content.SharedPreferences.Editor editor = mContextSp.edit();
                                        editor.putString(DeviceUtil.sbm, shebeiName.getText().toString());
                                        editor.commit();
                                        //FileHelper.putSDunique("iszhuce", FileHelper.iszhuce);
                                        LoginUtil.putIsZhuche(true);
                                        checkRegisterBinding();

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
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.this.dismissLoadingDialog();
                                Toast.makeText(LoginActivity.this, "对不起，注册失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }


    private void checkRegisterBinding() {
        //REQUEST_CODE_SEND_APP_ZHUCE
        //获取ip
        LoginActivity.this.dismissLoadingDialog();
        LoginActivity.this.showLoadingDialog("检查状态中！终端号：" + LoginUtil.getTerminalIdentity());
        ZLog.e("tag", "checkRegisterBinding");

        final HashMap hashMap = new HashMap();
        hashMap.put("terminalIdentity", LoginUtil.getTerminalIdentity());
        HttpClient.postHashMapEntity(AppUrl.checkRegisterBinding, hashMap, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(final MyApiResponse response) {
                super.onSuccess(response);
                ZLog.e("HashMap", response.toString());
              /*  {
                    "code": 0
                    返回码： 0是正常 ，非0，异常，参考msg的描述
-1 非法设备  1：设备未激活 2：非法签名　３：ｔｏｋｅｎ失效
                }*/
                if (response.code.equals("0")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            LoginActivity.this.dismissLoadingDialog();
                            LoginUtil.putIsZhuche(true);
                            getIp();
                        }
                    });
                } else {
                    LoginUtil.alarmSendReg();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // LoginActivity.this.dismissLoadingDialog();
                            if (response.code.equals("1")) {
                                Toast.makeText(AppContext.getInstance(), "设备未激活！", Toast.LENGTH_LONG).show();
                            } else if (response.code.equals("3")) {
                                Toast.makeText(AppContext.getInstance(), "token失效！", Toast.LENGTH_LONG).show();
                            } else if (response.code.equals("-1")) {
                                Toast.makeText(AppContext.getInstance(), "非法设备！", Toast.LENGTH_LONG).show();
                            } else if (response.code.equals("2")) {
                                Toast.makeText(AppContext.getInstance(), "非法签名！", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request request, final Exception e) {
                super.onFailure(request, e);
                ZLog.e("onFailure", "" + e.getMessage());
                LoginUtil.alarmSendReg();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.this.dismissLoadingDialog();
                        Toast.makeText(AppContext.getInstance(), "获取激活状态失败！" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void getIp() {
        //获取ip
        LoginActivity.this.showLoadingDialog("获取ip中..");
        HttpClient.postHashMapEntity(AppUrl.getServerList, new MyHttpResponseHandler() {
            @Override
            public void onSuccess(final MyApiResponse response) {
                super.onSuccess(response);
                LoginActivity.this.dismissLoadingDialog();
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
            public void onFailure(Request request, final Exception e) {
                super.onFailure(request, e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.this.dismissLoadingDialog();
                        Toast.makeText(AppContext.getInstance(), "获取ip失败激活！" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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
    }

    private void initView() {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onEvent(final Event mEvent) {
        switch (mEvent.getId()) {
            case EVENT_TEST_MSG_CHECKREGISTER:
                checkRegisterBinding();
                break;
        }
    }
}
