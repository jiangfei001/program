package com.example.testscreenshot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static int REQUEST_MEDIA_PROJECTION = 0;

    private Button mBtn;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = findViewById(R.id.btn);
        mImageView = findViewById(R.id.iv);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // try2StartScreenShot();

                boolean t = is_root();
                //boolean b = RootCmd.haveRoot();
                /*String a = getScreenshot();*/
                get_root();
                getScreenshot();
                Log.e("a", "string:");
            }
        });
    }


    // 获取ROOT权限
    public void get_root() {
        if (is_root()) {
            Toast.makeText(this, "已经具有ROOT权限!", Toast.LENGTH_LONG).show();
        } else {
            try {
                Runtime.getRuntime().exec("su");
            } catch (Exception e) {
                Toast.makeText(this, "获取ROOT权限时出错!", Toast.LENGTH_LONG).show();
            }
        }

    }

    // 判断是否具有ROOT权限
    public static boolean is_root() {
        boolean res = false;
        try {
            if ((!new File("/system/bin/su").exists()) &&
                    (!new File("/system/xbin/su").exists())) {
                res = false;
            } else {
                res = true;
            }
            ;
        } catch (Exception e) {

        }
        return res;
    }

    public void takeScreenShot() {
        String mSavedPath = Environment.getExternalStorageDirectory() + File.separator + "screenshot.png";
        try {
            Runtime.getRuntime().exec("screencap -p " + mSavedPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(mSavedPath);
        Log.e("f", file.getPath());
    }

    /**
     * 手机截图
     *
     * @return 返回截图的路径
     */
    public static String getScreenshot() {
        Process process = null;
        String mSavedPath = Environment.getExternalStorageDirectory() + File.separator + "jf.png";
        try {
            process = Runtime.getRuntime().exec("screencap -p " + mSavedPath);
            PrintStream outputStream = null;
            outputStream = new PrintStream(new BufferedOutputStream(process.getOutputStream(), 8192));
            outputStream.println("screencap -p " + mSavedPath);
            outputStream.flush();
            outputStream.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        File file = new File(mSavedPath);
        Log.e("sd", "df");
        return mSavedPath;
    }

    private void try2StartScreenShot() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK && data != null) {
                ScreenShotHelper screenShotHelper = new ScreenShotHelper(MainActivity.this, resultCode, data, new ScreenShotHelper.OnScreenShotListener() {
                    @Override
                    public void onFinish(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
                screenShotHelper.startScreenShot();
            }
        }
    }
}
