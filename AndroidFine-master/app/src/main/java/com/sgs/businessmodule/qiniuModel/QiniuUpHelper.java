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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 设备信息的工具类
 * <p>
 * Created by Beluga_白鲸.
 */

public class QiniuUpHelper {

   /* qiniu:
    accessKey: UxVCg4Xl0tmDmGPK2L_V9t3qH70gmONr1IbzdxuR
    secretKey: 4tD6wQSCc9lknYSucf_C4QZA1tavrUur0ntViHay
    bucket: multimedia-test
    path: http://q2jkye78n.bkt.clouddn.com*/
    /**
     * 签名
     */
    //private static String TOKEN = "cv6A8yeorA0LAoN11dboX2ybIFOBoEbe3C8ZN6PA:kcTCyjE33e1G-scr12X8ClLVkQk=:eyJzY29wZSI6Im11bHRpbWVkaWEyMDE5MTEiLCJyZXR1cm5VcmwiOiJodHRwOi8vcTB1OGhpamlsLmJrdC5jbG91ZGRuLmNvbS8iLCJyZXR1cm5Cb2R5Ijoie3N1Y2Vzczp4eHh9IiwiZGVhZGxpbmUiOjE1NzQ2Mjk5NTh9";

    /**
     * 上传数据到七牛云服务器
     *
     * @param activity View
     */
    public static void upload(Activity activity, boolean hasStatusBar, String Token, final TAKESCREEN.BackUrl backUrl) {
        UploadManager uploadManager = new UploadManager();
        Bitmap bitmap = DeviceUtil.snapCurrentScreenShot(activity, hasStatusBar);
        byte[] data = Bitmap2Bytes(bitmap);

        String strNow = new SimpleDateFormat("yyyyMMdd").format(new Date()).toString();

        String fileName = "/sreenshots/" + strNow.toString() + "/" + System.currentTimeMillis() + ".png";
        //data = "hello".getBytes();
        /*fileName = "hello.txt";*/

        uploadManager.put(data, fileName, Token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                Log.i("qiniu 访问链接 = ", key);
                if (info != null) {
                    Log.i("qiniu info = ", info.toString());
                }
                if (response != null) {
                    Log.i("qiniu response = ", response.toString());
                }
                backUrl.getUrlandName(key, info, response);
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


    public static void main(String[] args) {
        String strNow = new SimpleDateFormat("yyyyMMdd").format(new Date()).toString();

        String fileName = "/sreenshots/" + strNow.toString() + "/" + System.currentTimeMillis() + ".png";
        System.out.println(fileName);
    }
}


