package com.sgs.businessmodule.websocketmodel;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dalong.marqueeview.MarqueeView;
import com.jf.fine.R;
import com.sgs.AppContext;
import com.sgs.businessmodule.downloadModel.dbcontrol.FileHelper;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.businessmodule.taskModel.TaskQueue;
import com.sgs.businessmodule.taskModel.taskFactory.TaskFactory;
import com.sgs.businessmodule.taskUtil.cutMsg.MuTerminalMsg;
import com.sgs.middle.dbModel.entity.InstructionRequest;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.middle.utils.StringUtils;
import com.sgs.programModel.entity.ProgramResource;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocketActivityRelease extends EventActivity {

    private String TAG = "WebSocketActivity";

    /* private EditText etContent;
     private TextView tvMsg;
     private ScrollView scrollView;
 */
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

            final Timer connectionLostTimer = new Timer("WebSocketTimer");

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
            connectionLostTimer.scheduleAtFixedRate(connectionLostTimerTask, 10, 1000L * 50);


        }

        @Override
        public void onConnectFailed(Throwable e) {
            if (e != null) {
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
        getWvBookPlay().loadUrl("file:///android_asset/index.html");
        WebSocketHelper.initWebSocket(DeviceUtil.getUniqueID(WebSocketActivityRelease.this));
        WebSocketHandler.getDefault().addListener(socketListener);
        taskQueue = new TaskQueue(1);
        taskQueue.start();
        /*initSchedule();*/
        /*taskQueue = new TaskQueue(1);

        //从数据库加载所有未完成和未结束的任务
        InstructionRequestDao instructionRequestDao = new InstructionRequestDao(this);
        List<InstructionRequest> instructionRequests = instructionRequestDao.getAllTask();

        // 3. 使用Iterator迭代器
        Iterator<InstructionRequest> it = instructionRequests.iterator();
        while (it.hasNext()) {
            InstructionRequest instructionRequest = it.next();
            System.out.println(instructionRequest);
            TVTask tvTask = TaskFactory.createTask(instructionRequest);
            taskQueue.add(tvTask);
        }

        taskQueue.start();*/

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
    protected void onResume() {
        super.onResume();
        AppContext.getInstance().initFileService();
    }

    MarqueeView mMarqueeView1;
    MarqueeView mMarqueeView2;
    MarqueeView mMarqueeView3;

    private void initView() {
        wvBookPlay = (WebView) findViewById(R.id.activity_main_webview1);
        mMarqueeView1 = (MarqueeView) findViewById(R.id.mMarqueeView1);
        mMarqueeView2 = (MarqueeView) findViewById(R.id.mMarqueeView2);
        mMarqueeView3 = (MarqueeView) findViewById(R.id.mMarqueeView3);

        initweb(wvBookPlay);


        mMarqueeView1.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
            @Override
            public void onRollOver() {
                Log.e(TAG, "mMarqueeView1 onRollOver");
                mymHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        playNext();
                    }
                });
            }
        });
        mMarqueeView2.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
            @Override
            public void onRollOver() {
                Log.e(TAG, "mMarqueeView2 onRollOver");
                mymHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        playNext();
                    }
                });
            }
        });

        mMarqueeView3.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
            @Override
            public void onRollOver() {
                Log.e(TAG, "mMarqueeView3 onRollOver");
                mymHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        playNext();
                    }
                });
            }
        });

       /* findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgin = "{\n" +
                        "\t\"append\": 0,\n" +
                        "\t\"createTime\": 1580473823521,\n" +
                        "\t\"createUser\": \"admin\",\n" +
                        "\t\"deptId\": 2,\n" +
                        "\t\"fontColor\": \"#92e3ed\",\n" +
                        "\t\"fontSize\": \"100\",\n" +
                        "\t\"id\": 2,\n" +
                        "\t\"msgContent\": \"gdd 大范甘迪高蛋白\",\n" +
                        "\t\"playTimes\": 10,\n" +
                        "\t\"position\": 1,\n" +
                        "\t\"speed\": 2,\n" +
                        "\t\"status\": 0,\n" +
                        "\t\"terminalIds\": \"16,18\"\n" +
                        "}";

                MuTerminalMsg orderProgarmPalyInstructionVo = JSON.parseObject(orgin, new TypeReference<MuTerminalMsg>() {
                });

                Event event = new Event();
                HashMap<EventEnum, Object> params = new HashMap();
                params.put(EventEnum.EVENT_TEST_MSG1_KEY_CUTMSG, orderProgarmPalyInstructionVo);
                event.setParams(params);
                event.setId(EventEnum.EVENT_TEST_SETCUTMSG);
                EventBus.getDefault().post(event);

                System.out.println("orderProgarmPalyInstructionVo.toString()" + orderProgarmPalyInstructionVo.toString());
            }
        });

        findViewById(R.id.btn_send1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgin = "{\n" +
                        "\t\"append\": 1,\n" +
                        "\t\"createTime\": 1580473823521,\n" +
                        "\t\"createUser\": \"admin\",\n" +
                        "\t\"deptId\": 2,\n" +
                        "\t\"fontColor\": \"#92e3ed\",\n" +
                        "\t\"fontSize\": \"100\",\n" +
                        "\t\"id\": 2,\n" +
                        "\t\"msgContent\": \"阿斯顿发送到发送到发算地方\",\n" +
                        "\t\"playTimes\": 60,\n" +
                        "\t\"position\": 2,\n" +
                        "\t\"speed\": 2,\n" +
                        "\t\"status\": 0,\n" +
                        "\t\"terminalIds\": \"16,18\"\n" +
                        "}";

                MuTerminalMsg orderProgarmPalyInstructionVo = JSON.parseObject(orgin, new TypeReference<MuTerminalMsg>() {
                });

                Event event = new Event();
                HashMap<EventEnum, Object> params = new HashMap();
                params.put(EventEnum.EVENT_TEST_MSG1_KEY_CUTMSG, orderProgarmPalyInstructionVo);
                event.setParams(params);
                event.setId(EventEnum.EVENT_TEST_SETCUTMSG);
                EventBus.getDefault().post(event);

                System.out.println("orderProgarmPalyInstructionVo.toString()" + orderProgarmPalyInstructionVo.toString());
            }
        });

        findViewById(R.id.btn_send2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setId(EventEnum.EVENT_TEST_SETCLEARCUTMSG);
                EventBus.getDefault().post(event);
            }
        });*/

     /*   etContent = (EditText) findViewById(R.id.et_content);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        ;
        initweb(wvBookPlay);
        //webViewInit(wvBookPlay);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etContent.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(WebSocketActivityRelease.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                WebSocketHandler.getDefault().send(text);

              *//*  final InstructionResponse responseEntity = new InstructionResponse();
                responseEntity.setId(123);
                responseEntity.setExecuteTime(new Date());
                responseEntity.setResult("123123");
                responseEntity.setStatus(1);
                responseEntity.setReceiveTime(new Date());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient.postResponseEntity("http://192.168.0.97:8081/multimedia/api/terminal/callback", responseEntity, new HttpResponseHandler());
                    }
                }).start();*//*
            }
        });
        findViewById(R.id.btn_upqiniu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = DeviceUtil.snapCurrentWebViewShot(WebSocketActivityRelease.this.getWvBookPlay());
                Log.e("b", bitmap.toString());
                try {
                    // 指纹图片存放路径
                    String sdCardDir = FileHelper.getTempDirPath() + "/fingerprintimages/";
                    File dirFile = new File(sdCardDir);
                    if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                        dirFile.mkdirs();
                    }
                    File file = new File(sdCardDir, "dd.jpg");
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskProgarm.progarmTest1(DownLoadService.getDownLoadManager());
            }
        });
        findViewById(R.id.btn_down2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskProgarm.progarmTest1(DownLoadService.getDownLoadManager());
            }
        });
        findViewById(R.id.btn_down3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*SendToServerUtil.sendNowPro((ArrayList<ProgarmPalyInstructionVo>) ProgramScheduledManager.getInstance().getList());*//*
            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketHandler.getDefault().removeListener(socketListener);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void appendMsgDisplay(String msg) {
       /* StringBuilder textBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(tvMsg.getText())) {
            textBuilder.append("收到命令：" + tvMsg.getText().toString());
            textBuilder.append("\n");
        }
        textBuilder.append(msg);
        textBuilder.append("\n");
        tvMsg.setText(textBuilder.toString());
        tvMsg.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });*/
    }

    @Override
    public void onEvent(Event mEvent) {
        switch (mEvent.getId()) {
            case EVENT_TEST_MSG1:
                Log.d(this.getClass().getName(), "我收到播放消息啦1");
                wvBookPlay.getSettings().setJavaScriptEnabled(true);
                HashMap<EventEnum, Object> hashMap = mEvent.getParams();
                boolean isPlayMusic = (boolean) hashMap.get(EventEnum.EVENT_TEST_MSG2_KEY_ISPLAY_MUSIC);
                String path = (String) hashMap.get(EventEnum.EVENT_TEST_MSG2_KEY_HTML_PATH);
                if (isPlayMusic) {
                    //控制音乐的播放与暂停
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    } else {
                        //pause
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }
                    }
                }
                Log.e(TAG, "file://" + FileHelper.getFileDefaultPath() + "/" + path);
                wvBookPlay.loadUrl("file://" + FileHelper.getFileDefaultPath() + "/" + path);
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
                if (musicList != null && musicList.size() > 0) {
                    mediaPlayer.reset();
                    initMediaPlayer();
                    setComp();
                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }
                break;
            case EVENT_TEST_SETCUTMSG:
                Map event2 = mEvent.getParams();
                final MuTerminalMsg muTerminalMsg = (MuTerminalMsg) event2.get(EventEnum.EVENT_TEST_MSG1_KEY_CUTMSG);
                mymHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (muTerminalMsg.getAppend() == 0 || cutMsgList.size() == 0) {
                            resetCutMsg();
                            cutMsgList.add(muTerminalMsg);
                            Message msg1 = Message.obtain();
                            msg1.obj = muTerminalMsg;
                            msg1.what = 2;
                            Log.e(TAG, "muTerminalMsg.getPlayTimes()" + muTerminalMsg.getPlayTimes());
                            mymHandler.sendMessageDelayed(msg1, muTerminalMsg.getPlayTimes() * 1000);
                            Log.d(this.getClass().getName(), "EVENT_TEST_SETCUTMSG");
                            playNext();
                        } else {
                            Message msg1 = Message.obtain();
                            msg1.obj = muTerminalMsg;
                            msg1.what = 2;
                            mymHandler.sendMessageDelayed(msg1, muTerminalMsg.getPlayTimes() * 1000);
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
                        getWvBookPlay().loadUrl("file:///android_asset/index.html");
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

    public void playNext() {
        Log.e(TAG, "playNext" + cutMsgList.size());
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
        if (muTerminalMsg.getPosition() == 1) {
            mMarqueeView1.setVisibility(View.VISIBLE);
            mMarqueeView1.setSizeAndColor(muTerminalMsg.getFontSize(), muTerminalMsg.getFontColor());
            mMarqueeView1.setText(muTerminalMsg.getMsgContent());
            mMarqueeView1.setSep(muTerminalMsg.getSpeed());
            Log.e(TAG, "muTerminalMsg.getMsgContent():" + muTerminalMsg.getMsgContent());
            mMarqueeView1.startScroll();
        } else if (muTerminalMsg.getPosition() == 0) {
            mMarqueeView3.setVisibility(View.VISIBLE);
            mMarqueeView3.setSizeAndColor(muTerminalMsg.getFontSize(), muTerminalMsg.getFontColor());
            mMarqueeView3.setText(muTerminalMsg.getMsgContent());
            mMarqueeView3.setSep(muTerminalMsg.getSpeed());
            mMarqueeView3.startScroll();
        } else if (muTerminalMsg.getPosition() == 2) {
            mMarqueeView2.setVisibility(View.VISIBLE);
            mMarqueeView2.setSizeAndColor(muTerminalMsg.getFontSize(), muTerminalMsg.getFontColor());
            mMarqueeView2.setText(muTerminalMsg.getMsgContent());
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

    private void setComp() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicindex = musicindex + 1;
                if (musicindex >= musicList.size()) {
                    musicindex = 0;
                }
                initMediaPlayer();
                mediaPlayer.start();
            }
        });
    }
}
