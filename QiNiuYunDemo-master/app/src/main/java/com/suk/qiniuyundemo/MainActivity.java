package com.suk.qiniuyundemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {


    private static String accessKey="cv6A8yeorA0LAoN11dboX2ybIFOBoEbe3C8ZN6PA";
    private static String secretKey="q8I4a4ytWczGIcph85RyfYQYv9XBYVpqqQLRLy4Q";
    private static String bucket="multimedia201911";
    /**
     * 签名
     */
    private static String TOKEN = "cv6A8yeorA0LAoN11dboX2ybIFOBoEbe3C8ZN6PA:kcTCyjE33e1G-scr12X8ClLVkQk=:eyJzY29wZSI6Im11bHRpbWVkaWEyMDE5MTEiLCJyZXR1cm5VcmwiOiJodHRwOi8vcTB1OGhpamlsLmJrdC5jbG91ZGRuLmNvbS8iLCJyZXR1cm5Cb2R5Ijoie3N1Y2Vzczp4eHh9IiwiZGVhZGxpbmUiOjE1NzQ2Mjk5NTh9";
    /**
     * 上传地址
     */
    private static String HOST = "http://q0u8hijil.bkt.clouddn.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 上传数据到七牛云服务器
     *
     * @param view View
     */
    public void upload(View view) {
       /* UploadManager uploadManager = new UploadManager();*/
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        byte[] data = Bitmap2Bytes(bitmap);
        String fileName = "hello.aapng";

        //data = "hello".getBytes();
        /*fileName = "hello.txt";*/

      /*  uploadManager.put(data, fileName, TOKEN, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                Log.i("qiniu 访问链接 = ", HOST + key);
                Log.i("qiniu info = ", info.toString());
                Log.i("qiniu response = ", response.toString());
            }
        }, null);*/

    }

    public void uploadPic(View view)
    {
        //指定zone的具体区域
        //FixedZone.zone0   华东机房
        //FixedZone.zone1   华北机房
        //FixedZone.zone2   华南机房
        //FixedZone.zoneNa0 北美机房


    /*
    Configuration config = new Configuration.Builder()
            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
            .connectTimeout(10)           // 链接超时。默认10秒
            .useHttps(true)               // 是否使用https 默认是false
            .responseTimeout(60)          // 服务器响应超时。默认60秒
            .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
            .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
            .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
    */


        Configuration config = new Configuration.Builder()
                .useHttps(true)               // 是否使用https上传域名
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();

        UploadManager uploadManager = new UploadManager(config); // UploadManager对象只需要创建一次重复使用

        String data = "/storage/emulated/0/DCIM/Camera/IMG_20190623_172115.jpg"; //要上传的文件
        String key = "test111.jpg"; //在服务器的文件名

        /**
         * 生成token
         * create()方法的两个参数分别是 AK SK
         * uoloadToken()方法的参数是 要上传到的空间(bucket)
         */
        String token = Auth.create(accessKey, secretKey).uploadToken(bucket);

        Bitmap bitmap = DeviceUtil.snapCurrentScreenShot(MainActivity.this, true);

        byte[] data2 = Bitmap2Bytes(bitmap);
        /**
         * 调用put方法上传
         * 第一个参数 data：可以是字符串，是要上传图片的路径
         *                可以是File对象，是要上传的文件
         *                可以是byte[]数组，要是上传的数据
         * 第二个参数 key：字符串，是图片在服务器上的名称，要具有唯一性，可以用UUID
         * 第三个参数 token：根据开发者的 AK和SK 生成的token，这个token 应该在后端提供一个接口，然后android代码中发一个get请求获得这个tocken，但这里为了演示，直接写在本地了.
         * 第四个参数：UpCompletionHandler的实例，有个回调方法
         * 第五个参数：可先参数
         */
        uploadManager.put
                (
                        data2, key, token,
                        new UpCompletionHandler()
                        {
                            /**
                             * 回调方法
                             * @param key 开发者设置的 key 也就是文件名
                             * @param info 日志，包含上传的ip等
                             * @param res 包括一个hash值和key
                             */
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject res)
                            {
                                if(info.isOK())
                                {
                                    Log.i("上传结果：", "Upload Success");
                                }
                                else
                                {
                                    Log.i("上传结果：", "Upload Fail");
                                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                }
                                Log.i("key：", key + "\ninfo：" + info + "\nres：" + res);
                            }
                        },
                        null
                );
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
