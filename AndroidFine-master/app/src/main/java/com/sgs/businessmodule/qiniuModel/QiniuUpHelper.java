package com.sgs.businessmodule.qiniuModel;


import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.sgs.businessmodule.taskModel.taskList.TAKESCREEN;
import com.sgs.middle.utils.DateUtil;
import com.sgs.middle.utils.DeviceUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Date;


/**
 * 设备信息的工具类
 * <p>
 * Created by Beluga_白鲸.
 */

public class QiniuUpHelper {

    /**
     * 签名
     */
    private static String TOKEN = "cv6A8yeorA0LAoN11dboX2ybIFOBoEbe3C8ZN6PA:kcTCyjE33e1G-scr12X8ClLVkQk=:eyJzY29wZSI6Im11bHRpbWVkaWEyMDE5MTEiLCJyZXR1cm5VcmwiOiJodHRwOi8vcTB1OGhpamlsLmJrdC5jbG91ZGRuLmNvbS8iLCJyZXR1cm5Cb2R5Ijoie3N1Y2Vzczp4eHh9IiwiZGVhZGxpbmUiOjE1NzQ2Mjk5NTh9";
    /**
     * 上传地址
     */
    private static String HOST = "http://q0u8hijil.bkt.clouddn.com/";

    /**
     * 上传数据到七牛云服务器
     *
     * @param activity View
     */
    public static void upload(Activity activity, boolean hasStatusBar, final TAKESCREEN.BackUrl backUrl) {
        UploadManager uploadManager = new UploadManager();
        Bitmap bitmap = DeviceUtil.snapCurrentScreenShot(activity, hasStatusBar);
        byte[] data = Bitmap2Bytes(bitmap);

        String fileName = DateUtil.getNowDate() + ".png";
        //data = "hello".getBytes();
        /*fileName = "hello.txt";*/

        uploadManager.put(data, fileName, TOKEN, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                Log.i("qiniu 访问链接 = ", HOST + key);
                Log.i("qiniu info = ", info.toString());
                Log.i("qiniu response = ", response.toString());
                backUrl.getUrlandName(info.toString());
            }
        }, null);
    }


    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}


