package com.sgs.businessmodule.websocketmodel;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 *
 * Handler工具
 * 1.生成一个唯一的ID，App全局唯一，这样可以防止可能发生的消息ID重复而导致的各种问题。
 * 2.静态Handler接口， 用来消除Handle可能导致的泄漏。
 *
 * Created by weatherfish  (01174599) on 2016/9/26.
 */

public class HandlerUtil {
    private static int mId = 0x1000000;

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    public interface MessageListener {
        public void handleMessage(Message msg);
    }

    /**
     * 生成一个唯一的Message ID，App全局唯一，这样可以防止可能发生的消息ID重复而导致的各种问题。
     * @return
     */
    public static final int generateId() {
        return ++ mId;
    }

    /**
     * 用来消除Handle可能导致的泄漏
     *
     */
    public static class StaticHandler extends Handler {
        WeakReference<MessageListener> listener;

        /**
         *
         * @param listener
         *            必读: 此listener必须由Activity实现该接口(推荐)或者是宿主Activity的类成员 :
         *            这里是弱引用, 不会增加变量的引用计数, 使用匿名变量会导致listener过早释放(请参考此类的引用示例)
         */
        public StaticHandler(MessageListener listener) {
            super();
            this.listener = new WeakReference<MessageListener>(listener);
        }

        public StaticHandler(Looper looper, MessageListener listener){
            super(looper);
            this.listener = new WeakReference<MessageListener>(listener);
        }

        public StaticHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            MessageListener messageListener = this.listener.get();
            if (messageListener != null) {
                messageListener.handleMessage(msg);
            }
        }
    }

    /**
     * 在UI线程延时运行
     *
     * @param runnable
     */
    public static void runUITask(Runnable runnable, long delayTime) {
        uiHandler.postDelayed(runnable, delayTime);
    }

}

