package com.sgs.businessmodule.websocketmodel;

import android.content.Context;
import android.util.Log;

import com.sgs.AppContext;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by 01174599 on 2017/1/10.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息

    /****************************
     * 友盟自定义事件相关字段定义开始
     ****************************/
    //友盟自定义事件名称
    public static final String CUSTOM_EVENT_PLUGIN_VER_EXCEPTION = "CUSTOM_EVENT_PLUGIN_VER_EXCEPTION";
    //网点
    public static final String AREA_CODE = "AREA_CODE";
    //区域码
    public static final String AREA_ID = "AREA_ID";
    //工号
    public static final String USER_NAME = "USER_NAME";
    //异常
    public static final String THROWABLE_STR = "THROWABLE_STR";

    /****************************
     * 友盟自定义事件相关字段定义结束
     ****************************/
    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void initCrashHandlerException(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //自定义处理，传递异常处理后，延迟1s进行全部进程杀死
            mDefaultHandler.uncaughtException(thread, ex);
            AppContext.getInstance().exitApp();
           /* HandlerUtil.runUITask(new Runnable() {
                @Override
                public void run() {
                    ActivityLifeManager.getInstance().restartAPP(mContext);
                }
            }, 1000);*/
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        MobclickAgent.reportError(mContext, "工号" + AppContext.getInstance().gonghao + "message:" + ex.getMessage());//errorContent是String格式
        Log.e("handleException", "handleException");
        //保存日志文件
       /* MainLogUtils.logWithCaller(ex,6,Long.toString(System.currentTimeMillis()));
        //uniteMain日志太多奇怪的日志了，新增一个专门保存crash的
        CrashLogUtils.logWithCaller(ex,6,Long.toString(System.currentTimeMillis()));

        //输出所有缓存的日志
        FileLogFactory.finalWriteAll();*/
        return true;
    }

}