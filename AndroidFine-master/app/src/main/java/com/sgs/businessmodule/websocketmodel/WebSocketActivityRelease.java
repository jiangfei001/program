package com.sgs.businessmodule.websocketmodel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dalong.marqueeview.MarqueeView;
import com.jf.fine.R;
import com.sgs.AppContext;
import com.sgs.businessmodule.downloadModel.dbcontrol.FileHelper;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskModel.TaskQueue;
import com.sgs.businessmodule.taskModel.taskFactory.TaskFactory;
import com.sgs.businessmodule.taskUtil.cutMsg.MsgDbManager;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg;
import com.sgs.businessmodule.websocketmodel.js.JsInterface;
import com.sgs.middle.dbModel.entity.InstructionRequest;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.middle.utils.InstallUtil;
import com.sgs.middle.utils.StringUtils;
import com.sgs.programModel.entity.ProgramResource;
import com.umeng.analytics.MobclickAgent;
import com.jf.websocket.SimpleListener;
import com.jf.websocket.SocketListener;
import com.jf.websocket.WebSocketHandler;
import com.jf.websocket.response.ErrorResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocketActivityRelease extends EventActivity {

    private String TAG = "WebSocketActivityRelease";

    public WebView getWvBookPlay() {
        return wvBookPlay;
    }

    public void setWvBookPlay(WebView wvBookPlay) {
        this.wvBookPlay = wvBookPlay;
    }

    WebView wvBookPlay;

    private MediaPlayer mediaPlayer = new MediaPlayer();//实例化对象

    private SocketListener socketListener = new SimpleListener() {
        @Override
        public void onConnected() {
            appendMsgDisplay("onConnected");
            DeviceUtil.setConnectionTime();
            Log.e(TAG, "onConnected");
            /* final Timer connectionLostTimer = new Timer("WebSocketTimer");

               TimerTask connectionLostTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (WebSocketHandler.getDefault().isConnect()) {
                        WebSocketHandler.getDefault().send("xintiao");
                    } else {
                        connectionLostTimer.cancel();
                    }
                    Log.e(TAG, "我是心跳");
                }
            };

            //scheduleAtFixedRate
            connectionLostTimer.scheduleAtFixedRate(connectionLostTimerTask, 10, 1000L * 60);*/

        }

        @Override
        public void onConnectFailed(Throwable e) {
            Log.e(TAG, "Throwable");
            if (e != null) {
                Log.e(TAG, "连接onConnectFailed" + e.toString());
                appendMsgDisplay("onConnectFailed:" + e.toString());
            } else {
                appendMsgDisplay("onConnectFailed:null");
            }
        }

        @Override
        public void onDisconnect() {
            appendMsgDisplay("onDisconnect");
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            appendMsgDisplay(errorResponse.getDescription());
            errorResponse.release();
        }

        @Override
        public <T> void onMessage(String message, T data) {
            if (data instanceof InstructionRequest) {
                appendMsgDisplay(data.toString());
                Log.e(TAG, data.toString());
                final InstructionRequest requestEntity = (InstructionRequest) data;
                TVTask tvTask = TaskFactory.createTask(requestEntity);
                taskQueue.add(tvTask);
            }
        }
    };

    TaskQueue taskQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket1);
        initView();
        AppContext.getInstance().islogin = true;

        getWvBookPlay().loadUrl("file:///android_asset/index.html");
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppContext.getInstance().initFileService();

                WebSocketHelper.initWebSocket(DeviceUtil.getTerDeviceID(WebSocketActivityRelease.this));
                WebSocketHandler.getDefault().addListener(socketListener);
                taskQueue = new TaskQueue(1);
                taskQueue.start();
                cutMsgList = MsgDbManager.getInstance().getAllMuTerminalMsg();
                if (cutMsgList != null && cutMsgList.size() > 0) {
                    Log.e(TAG, "我是从数据库中获取的消息列表" + cutMsgList.size());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            playNext();
                        }
                    });
                } else {
                    cutMsgList = new LinkedList<>();
                }
            }
        }).start();

    }

    Handler handler = new Handler();

    @Override
    protected void onResume() {
        super.onResume();

    }


    private class InnerWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /**
         * 处理ssl请求
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        /**
         * 页面载入完成回调
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:try{autoplay();}catch(e){}");//播放视频
        }

    }

    private void initweb(WebView mWebView) {
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebViewClient(new InnerWebViewClient());
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 这句解决本地跨域问题，如果你的 PDF 文件在站点里，是不需要的，但是，我们一般情况是加载站点外部 PDF 文件
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new JsInterface(this), "native");
        mWebView.setScrollContainer(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            /**
             * 显示自定义视图，无此方法视频不能播放
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    MarqueeView mMarqueeView1;
    MarqueeView mMarqueeView2;
    MarqueeView mMarqueeView3;
    private TextView tvMsg;

    private void initView() {
        wvBookPlay = (WebView) findViewById(R.id.activity_main_webview1);
        mMarqueeView1 = (MarqueeView) findViewById(R.id.mMarqueeView1);
        mMarqueeView2 = (MarqueeView) findViewById(R.id.mMarqueeView2);
        mMarqueeView3 = (MarqueeView) findViewById(R.id.mMarqueeView3);
        final ScrollView scroll_view = (ScrollView) findViewById(R.id.scroll_view);
        initweb(wvBookPlay);
        tvMsg = (TextView) findViewById(R.id.tv_msg);

        /*wvBookPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scroll_view.getVisibility() == View.VISIBLE) {
                    scroll_view.setVisibility(View.INVISIBLE);
                } else {
                    scroll_view.setVisibility(View.VISIBLE);
                }
            }
        });*/

        mMarqueeView1.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
            @Override
            public void onRollOver() {
                Log.e(TAG, "mMarqueeView1 onRollOver");
                gotoPlay();
            }
        });
        mMarqueeView2.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
            @Override
            public void onRollOver() {
                Log.e(TAG, "mMarqueeView2 onRollOver");
                gotoPlay();
            }
        });

        mMarqueeView3.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
            @Override
            public void onRollOver() {
                Log.e(TAG, "mMarqueeView3 onRollOver");
                gotoPlay();
            }
        });
    }

    private void gotoPlay() {
        mymHandler.post(new Runnable() {
            @Override
            public void run() {
                if (nowPlayTime != 0) {
                    Long totleTime = (System.currentTimeMillis() - nowPlayTime) / 1000;
                    Log.e("totleTime", "totleTime" + totleTime);
                    if (nowTerminalMsg != null) {
                        Long integer = nowTerminalMsg.getHasplay();
                        nowTerminalMsg.setHasplay(totleTime + integer);
                        Log.e(TAG, "mMarqueeView3 getHasplay" + nowTerminalMsg.getHasplay() + "content" + nowTerminalMsg.getMsgContent());
                        if (nowTerminalMsg.getHasplay() >= nowTerminalMsg.getPlayTimes()) {
                            Log.e(TAG, "mMarqueeView3 getHasplay" + nowTerminalMsg.getHasplay() + "getPlayTimes:" + nowTerminalMsg.getPlayTimes());
                            if (cutMsgList != null && cutMsgList.size() > 0) {
                                cutMsgList.remove(nowTerminalMsg);
                                MsgDbManager.getInstance().delByMuTerminalMsgID(nowTerminalMsg.getId());
                                nowTerminalMsg = null;
                                nowCutMsgIndex--;
                            }
                        }
                    }
                }
                playNext();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketHandler.getDefault().removeListener(socketListener);
        clearMediaPlayer();
        AppContext.getInstance().exitApp();
    }


    public void clearMediaPlayer() {
        if (musicList != null) {
            musicList.clear();
        }
        musicindex = 0;
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            }
        } catch (IllegalStateException e) {
            Log.e("IllegalStateException", e.getMessage() + "11");
        }
    }

    private void appendMsgDisplay(String msg) {
        StringBuilder textBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(tvMsg.getText())) {
            textBuilder.append("收到命令：" + tvMsg.getText().toString());
            textBuilder.append("\n");
        }
        textBuilder.append(msg);
        textBuilder.append("\n");
        tvMsg.setText(textBuilder.toString());

    }

    @Override
    public void onEvent(final Event mEvent) {
        switch (mEvent.getId()) {
            case EVENT_TEST_MSG1:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(this.getClass().getName(), "我收到播放消息啦1");
                        /*wvBookPlay.getSettings().setJavaScriptEnabled(true);*/
                        HashMap<EventEnum, Object> hashMap = mEvent.getParams();
                        boolean isPlayMusic = (boolean) hashMap.get(EventEnum.EVENT_TEST_MSG2_KEY_ISPLAY_MUSIC);
                        String path = (String) hashMap.get(EventEnum.EVENT_TEST_MSG2_KEY_HTML_PATH);
                        Log.e(TAG, "isPlayMusic:" + isPlayMusic);
                        if (isPlayMusic) {
                            //需要播放，则开始播放
                            Log.e(TAG, "这个场景需要播放音乐");
                            try {
                                if (mediaPlayer == null) {
                                    mediaPlayer = new MediaPlayer();
                                }
                                boolean isplay = false;
                                try {
                                    isplay = mediaPlayer.isPlaying();
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                                if (!isplay && (musicList != null && musicList.size() > 0)) {
                                    Log.e(TAG, "音乐开始执行");
                                    mediaPlayer.start();
                                }
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //不需要播放，则要暂停播放
                            Log.e(TAG, "这个场景不需要播放音乐");
                            try {
                                if (mediaPlayer != null) {
                                    Log.e(TAG, "isPlayMusic:pause2" + mediaPlayer.isPlaying());
                                    if (mediaPlayer.isPlaying()) {
                                        Log.e(TAG, "isPlayMusic:pause3");
                                        mediaPlayer.pause();
                                    }
                                }
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e(TAG, "file://" + FileHelper.getFileDefaultPath() + "/" + path);
                        wvBookPlay.loadUrl("file://" + FileHelper.getFileDefaultPath() + "/" + path);
                    }
                });

                break;
            case EVENT_TEST_MSG2:
                Map event = mEvent.getParams();
                String msg = (String) event.get(EventEnum.EVENT_TEST_MSG2_KEY);
                appendMsgDisplay(msg);
                break;
            case EVENT_TEST_SETMUSIC:
                Log.d(this.getClass().getName(), "我收到消息啦1EVENT_TEST_SETMUSIC");
                Map event1 = mEvent.getParams();
                musicList = (List<ProgramResource>) event1.get(EventEnum.EVENT_TEST_MSG2_KEY_MUSIC);
                //reset播放
                boolean isPlay = false;
                try {
                    if (mediaPlayer != null) {
                        isPlay = mediaPlayer.isPlaying();
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "这个节目有音乐isPlay" + isPlay);
                if (mediaPlayer != null && isPlay) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                if (musicList != null && musicList.size() > 0) {
                    Log.d(TAG, "这个节目有音乐" + musicList.size());
                    mediaPlayer = new MediaPlayer();
                    initMediaPlayer();
                    setComp();
                } else {
                    Log.e(TAG, "这个节目没有音乐要播放的");
                }
                break;
            case EVENT_TEST_SETCUTMSG:
                Map event2 = mEvent.getParams();
                final MuTerminalMsg muTerminalMsg = (MuTerminalMsg) event2.get(EventEnum.EVENT_TEST_MSG1_KEY_CUTMSG);

                mymHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (muTerminalMsg.getAppend() == 0) {
                            MsgDbManager.getInstance().delAllMuTerminalMsg();
                        }
                        MsgDbManager.getInstance().saveMuTermianlMsg(muTerminalMsg);
                        List<MuTerminalMsg> l = MsgDbManager.getInstance().getAllMuTerminalMsg();
                        Log.e("l", "lfff" + l.size());
                        if (muTerminalMsg.getAppend() == 0 || cutMsgList.size() == 0) {
                            resetCutMsg();
                            cutMsgList.add(muTerminalMsg);
                            Log.d(this.getClass().getName(), "EVENT_TEST_SETCUTMSG");
                            playNext();
                        } else {
                            Log.d(this.getClass().getName(), "EVENT_TEST_SETCUTMSG");
                            cutMsgList.add(muTerminalMsg);
                        }
                    }
                });
                break;
            case EVENT_TEST_SETCLEARCUTMSG:
                mymHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MsgDbManager.getInstance().delAllMuTerminalMsg();
                        resetCutMsg();
                        Log.d(this.getClass().getName(), "EVENT_TEST_SETCLEARCUTMSG");
                    }
                });
                break;
            case EVENT_TEST_DELETECUTMSG:
                Log.d(this.getClass().getName(), "我收到消息啦1EVENT_TEST_SETMUSIC");
                Map eventDel = mEvent.getParams();
                ArrayList<Integer> arrayList = (ArrayList<Integer>) eventDel.get(EventEnum.EVENT_TEST_MSG1_KEY_DELETECUTMSG);
                if (arrayList != null && arrayList.size() > 0) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        int index = arrayList.get(i);
                        Iterator<MuTerminalMsg> iterator = cutMsgList.iterator();
                        while (iterator.hasNext()) {
                            MuTerminalMsg value = iterator.next();
                            if (value.getId() == index) {
                                iterator.remove();
                                Log.e("======", value + "已经移除");
                            }
                        }
                    }
                }
                break;
            case EVENT_TEST_CLEARPROG:
                mymHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        clearMediaPlayer();
                        getWvBookPlay().loadUrl("file:///android_asset/index.html");
                    }
                });
                break;
            case EVENT_TEST_INSTALL:
                mymHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Map eventDel = mEvent.getParams();
                        String path = (String) eventDel.get(EventEnum.EVENT_TEST_MSG1_KEY_PATH);
                        mInstallUtil = new InstallUtil(WebSocketActivityRelease.this, path);
                        mInstallUtil.install();
                    }
                });
                break;
        }
    }

    //新建Handler对象。
    Handler mymHandler = new Handler() {
        //handleMessage为处理消息的方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                Log.e(TAG, "msg.what=2");
                MuTerminalMsg muTerminalMsg = (MuTerminalMsg) msg.obj;
                if (muTerminalMsg != null) {
                    Log.e(TAG, "DelMuTerminalMsg:" + muTerminalMsg.getMsgContent());
                    cutMsgList.remove(muTerminalMsg);
                }
            }
        }
    };

    public Long nowPlayTime;
    public MuTerminalMsg nowTerminalMsg;

    public void playNext() {
        Log.e(TAG, "playNext" + cutMsgList.size());
        nowPlayTime = System.currentTimeMillis();

        if (cutMsgList.size() <= 0) {
            resetCutMsg();
            return;
        }
       /* mMarqueeView1.reset();
        mMarqueeView2.reset();
        mMarqueeView3.reset();*/
        if (nowCutMsgIndex >= cutMsgList.size() - 1) {
            nowCutMsgIndex = 0;
        } else {
            nowCutMsgIndex++;
        }

        Log.e(TAG, "playNext" + nowCutMsgIndex);
        MuTerminalMsg muTerminalMsg = cutMsgList.get(nowCutMsgIndex);
        nowTerminalMsg = muTerminalMsg;


        /*StringBuilder sb = new StringBuilder(nowTerminalMsg.getMsgContent());
        String content = sb.reverse().toString();
        Log.e(TAG, "context" + content);*/

        if (muTerminalMsg.getPosition() == 1) {
            mMarqueeView1.setVisibility(View.VISIBLE);
            mMarqueeView1.setSizeAndColor(muTerminalMsg.getFontSize(), muTerminalMsg.getFontColor());
            mMarqueeView1.setText(nowTerminalMsg.getMsgContent());
            mMarqueeView1.setSep(muTerminalMsg.getSpeed());
            Log.e(TAG, "muTerminalMsg.getMsgContent():" + muTerminalMsg.getMsgContent());
            mMarqueeView1.startScroll();
        } else if (muTerminalMsg.getPosition() == 0) {
            mMarqueeView3.setVisibility(View.VISIBLE);
            mMarqueeView3.setSizeAndColor(muTerminalMsg.getFontSize(), muTerminalMsg.getFontColor());
            mMarqueeView3.setText(nowTerminalMsg.getMsgContent());
            mMarqueeView3.setSep(muTerminalMsg.getSpeed());
            mMarqueeView3.startScroll();
        } else if (muTerminalMsg.getPosition() == 2) {
            mMarqueeView2.setVisibility(View.VISIBLE);
            mMarqueeView2.setSizeAndColor(muTerminalMsg.getFontSize(), muTerminalMsg.getFontColor());
            mMarqueeView2.setText(nowTerminalMsg.getMsgContent());
            mMarqueeView2.setSep(muTerminalMsg.getSpeed());
            mMarqueeView2.startScroll();
        }
    }

    public void resetCutMsg() {
        Log.e(TAG, "resetCutMsg");
        nowCutMsgIndex = 0;
        cutMsgList.clear();
        mymHandler.removeMessages(2);
        mMarqueeView1.setVisibility(View.INVISIBLE);
        mMarqueeView2.setVisibility(View.INVISIBLE);
        mMarqueeView3.setVisibility(View.INVISIBLE);
        mMarqueeView1.stopScroll();
        mMarqueeView2.stopScroll();
        mMarqueeView3.stopScroll();
        /*mMarqueeView1.reset();
        mMarqueeView2.reset();
        mMarqueeView3.reset();*/
       /* mMarqueeView1.stopScroll();
        mMarqueeView2.stopScroll();
        mMarqueeView3.stopScroll();
        mMarqueeView1.reset();
        mMarqueeView2.reset();
        mMarqueeView3.reset();*/
    }


    List<MuTerminalMsg> cutMsgList = new LinkedList<>();
    int nowCutMsgIndex = 0;

    List<ProgramResource> musicList;
    int musicindex = 0;
    /**
     * 规定开始音乐、暂停音乐、结束音乐的标志
     */
    public static final int PLAT_MUSIC = 1;
    public static final int PAUSE_MUSIC = 2;
    public static final int STOP_MUSIC = 3;

    private void initMediaPlayer() {
        try {
            String path = null;
            ProgramResource programResource = musicList.get(musicindex);
            if (!StringUtils.isEmpty(programResource.getVirtualPath())) {
                path = "file://" + FileHelper.getFileDefaultPath() + "/" + programResource.getVirtualPath();
                Log.e(TAG, "path111" + path);
            }//想要添加判断 是否找到music.map3
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            //为播放器添加播放完成时的监听器
        } catch (Exception e) {
            Toast.makeText(WebSocketActivityRelease.this, "the sd have not music.mp3" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private InstallUtil mInstallUtil;

    private void setComp() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e(TAG, "音乐播放完成");
                if (musicList == null || musicList.size() <= 0) {
                    return;
                }
                musicindex = musicindex + 1;
                if (musicindex >= musicList.size()) {
                    musicindex = 0;
                }
                Log.e(TAG, "开始播放音乐序号为" + musicindex + "总共的音乐数目为：" + musicList.size());
                initMediaPlayer();
                mediaPlayer.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == InstallUtil.UNKNOWN_CODE) {
            mInstallUtil.install();//再次执行安装流程，包含权限判等
        }
    }
}
