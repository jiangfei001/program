package com.sgs.middle.jiepin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhangke.zlog.ZLog;

public class ScreentShotUtil {

    private static final String TAG = "ScreentShotUtil";

    private static final String CLASS1_NAME = "android.view.SurfaceControl";

    private static final String CLASS2_NAME = "android.view.Surface";

    private static final String METHOD_NAME = "screenshot";

    private static ScreentShotUtil instance;

    private Display mDisplay;

    private DisplayMetrics mDisplayMetrics;

    private Matrix mDisplayMatrix;

    private WindowManager wm;

    private SimpleDateFormat format;

    private ScreentShotUtil() {

    }

    public static ScreentShotUtil getInstance() {
        synchronized (ScreentShotUtil.class) {
            if (instance == null) {
                instance = new ScreentShotUtil();
            }
        }
        return instance;
    }

    public Bitmap screenShot(int width, int height) {
        ZLog.e(TAG, "android.os.Build.VERSION.SDK : " + android.os.Build.VERSION.SDK_INT);
        Class<?> surfaceClass = null;
        Method method = null;
        try {
            ZLog.e(TAG, "width : " + width);
            ZLog.e(TAG, "height : " + height);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {

                surfaceClass = Class.forName(CLASS1_NAME);
            } else {
                surfaceClass = Class.forName(CLASS2_NAME);
            }
            method = surfaceClass.getDeclaredMethod(METHOD_NAME, int.class, int.class);
            method.setAccessible(true);
            return (Bitmap) method.invoke(null, width, height);
        } catch (NoSuchMethodException e) {
            ZLog.e(TAG, e.toString());
        } catch (IllegalArgumentException e) {
            ZLog.e(TAG, e.toString());
        } catch (IllegalAccessException e) {
            ZLog.e(TAG, e.toString());
        } catch (InvocationTargetException e) {
            ZLog.e(TAG, e.toString());
        } catch (ClassNotFoundException e) {
            ZLog.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * Takes a screenshot of the current display and shows an animation.
     */
    @SuppressLint("NewApi")
    public Bitmap takeScreenshot(final Context context, String fileFullPath) {
       /* if (fileFullPath == "") {
            return;
            *//*format = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = format.format(new Date(System.currentTimeMillis())) + ".png";
            fileFullPath = "/data/local/tmp/" + fileName;*//*
        }*/
/*
        if (ShellUtils.checkRootPermission()) {
            ZLog.e(TAG, "startsWith:checkRootPermission true;");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                ZLog.e(TAG, "/system/bin/screencap -p");
                ShellUtils.execCommand("/system/bin/screencap -p " + fileFullPath, true);
            }
        } else {*/
        // if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        ZLog.e(TAG, "android.os.Build.VERSION_CODES.JELLY_BEAN_MR2");
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();
        mDisplayMatrix = new Matrix();
        mDisplayMetrics = new DisplayMetrics();
        // We need to orient the screenshot correctly (and the Surface api seems to take screenshots
        // only in the natural orientation of the device :!)
        mDisplay.getRealMetrics(mDisplayMetrics);
        float[] dims =
                {
                        mDisplayMetrics.heightPixels, mDisplayMetrics.widthPixels
                };
        float degrees = getDegreesForRotation(mDisplay.getRotation());
        ZLog.e(TAG, "degrees" + degrees);
        /*boolean requiresRotation = (degrees > 0);
        if (requiresRotation) {
            // Get the dimensions of the device in its native orientation
            mDisplayMatrix.reset();
            mDisplayMatrix.preRotate(-degrees);
            mDisplayMatrix.mapPoints(dims);
            dims[0] = Math.abs(dims[0]);
            dims[1] = Math.abs(dims[1]);
        }*/
        ZLog.e(TAG, "dims:" + dims[0] + "dims" + dims[1]);
        Bitmap mScreenBitmap = screenShot((int) dims[0], (int) dims[1]);
        ZLog.e(TAG, "mScreenBitmap"+mScreenBitmap);
        Bitmap ss = null;
   /*     if (requiresRotation) {
            ZLog.e(TAG, "requiresRotation");
            // Rotate the screenshot to the current orientation
            ss = Bitmap.createBitmap(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(ss);
            c.translate(ss.getWidth() / 2, ss.getHeight() / 2);
            c.rotate(degrees);
            c.translate(-dims[0] / 2, -dims[1] / 2);
            c.drawBitmap(mScreenBitmap, 0, 0, null);
            c.setBitmap(null);
        }*/
        return ss;
            /* // If we couldn't take the screenshot, notify the user
            if (mScreenBitmap == null) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "screen shot fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            // Optimizations
            mScreenBitmap.prepareToDraw();
            ZLog.e(TAG, "mScreenBitmap" + mScreenBitmap.getAllocationByteCount());
            saveBitmap2file(context, mScreenBitmap, fileFullPath);*/
        // }
        //  }
    }

    /**
     * 根据 路径 得到 file 得到 bitmap
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Bitmap decodeFile(String filePath) throws IOException {
        Bitmap b = null;
        int IMAGE_MAX_SIZE = 600;

        File f = new File(filePath);
        if (f == null) {
            return null;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
        return b;
    }

    //
    public void saveBitmap2file(Context context, Bitmap bmp, String fileName) {
        int quality = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        byte[] buffer = new byte[1024];
        int len = 0;
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdir();
                file.getParentFile().createNewFile();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            try {
                file.getParentFile().delete();
                file.getParentFile().createNewFile();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            while ((len = is.read(buffer)) != -1) {
                stream.write(buffer, 0, len);
            }
            stream.flush();
        } catch (FileNotFoundException e) {
            Log.i(TAG, e.toString());
        } catch (IOException e) {
            Log.i(TAG, e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.i(TAG, e.toString());
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.i(TAG, e.toString());
                }
            }
        }
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
        }
    }

    /**
     * @return the current display rotation in degrees
     */
    private float getDegreesForRotation(int value) {
        switch (value) {
            case Surface.ROTATION_90:
                return 360f - 90f;
            case Surface.ROTATION_180:
                return 360f - 180f;
            case Surface.ROTATION_270:
                return 360f - 270f;
        }
        return 0f;
    }

}
