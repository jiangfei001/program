package com.sgs.middle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import com.zhangke.zlog.ZLog;

import com.sgs.AppContext;
import com.sgs.AppUrl;
import com.sgs.businessmodule.httpModel.HttpClient;
import com.sgs.businessmodule.httpModel.MyApiResponse;
import com.sgs.businessmodule.httpModel.MyHttpResponseHandler;
import com.sgs.middle.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

public class PackageReceiver extends BroadcastReceiver {
    private static final String TAG = "PackageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getDataString();
        ZLog.e("PRA1", "PackageReceiver");
        // 安装
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            ZLog.e("PRA", "PackageReceiver");
            sendApps();
        }
        // 覆盖安装
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            ZLog.e("PRA", "PackageReceiver");
            sendApps();
        }
        // 移除
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            ZLog.e("PRA", "PackageReceiver");
            sendApps();
        }
    }

    //查询已安装的APP
    public static void sendApps() {
        ZLog.e("sendApps", "sendApps");
        /*参数：  terminalIdentity设备唯一码
        apkListJson    String

        apkListJson参数例子
        [{"appName":" appName 1","packName":"com.fd.asdf.df","edition":"1.0"},
        {"appName":" appName ","packName":"com.fd.asdf.df","edition":"1.0"},
        {"appName":" appName ","packName":"com.fd.asdf.df","edition":"1.0"}]*/

        if (AppContext.islogin) {
            ArrayList<Install> res = new ArrayList<Install>();
            // 获取手机应用的集合
            List<PackageInfo> packs = AppContext.getInstance().getPackageManager()
                    .getInstalledPackages(0);
            ZLog.e("PackageReceiver", "PackageReceiver" + packs.size());
            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);
                // 定义应用bean对象
                Install newInfo = new Install();
                // 应用名
                newInfo.setAppName(p.applicationInfo.loadLabel(
                        AppContext.getInstance().getPackageManager()).toString());
                // 包名
                newInfo.setPackName(p.packageName);
                // 获取清单文件的versionCode版本号
                newInfo.setEdition(p.versionCode + "");
                res.add(newInfo);
            }

            final HashMap hashMap = new HashMap();

            hashMap.put("terminalIdentity", DeviceUtil.getTerDeviceID(AppContext.getInstance()));

            hashMap.put("apkListJson", com.alibaba.fastjson.JSON.toJSONString(res));

            ZLog.e("HashMap", hashMap.toString());

            HttpClient.postHashMapEntity(AppUrl.updateApkJson, hashMap, new MyHttpResponseHandler() {
                @Override
                public void onSuccess(MyApiResponse response) {
                    ZLog.e(TAG, "sendEventupdateApkJson onSuccess" + response.msg);
                }

                @Override
                public void onFailure(Request request, Exception e) {
                    ZLog.e(TAG, "sendEventupdateApkJson onSuccess");
                }
            });
        }
    }
}
