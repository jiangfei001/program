package com.sgs.businessmodule.websocketmodel;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgs.AppContext;
import com.sgs.businessmodule.taskModel.taskList.TAKESCREEN;
import com.sgs.middle.dbModel.entity.InstructionRequest;
import com.sgs.businessmodule.downloadModel.DownLoadService;
import com.sgs.businessmodule.downloadModel.dbcontrol.FileHelper;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;
import com.sgs.programModel.entity.ProgramResource;
import com.sgs.businessmodule.qiniuModel.QiniuUpHelper;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.programModel.TaskProgarm;
import com.sgs.businessmodule.taskModel.TaskQueue;
import com.sgs.businessmodule.taskModel.taskFactory.TaskFactory;
import com.sgs.middle.utils.StringUtils;
import com.yuzhi.fine.R;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketActivity extends EventActivity {

    private String TAG = "WebSocketActivity";
    private EditText etContent;
    private TextView tvMsg;
    private ScrollView scrollView;
    WebView wvBookPlay;

    private MediaPlayer mediaPlayer = new MediaPlayer();//实例化对象

    private SocketListener socketListener = new SimpleListener() {
        @Override
        public void onConnected() {
            appendMsgDisplay("onConnected");
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
        setContentView(R.layout.activity_web_socket);
        initView();
        WebSocketHelper.initWebSocket(AppContext.getInstance().getUserName);
        WebSocketHandler.getDefault().addListener(socketListener);
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

    private void initView() {
        etContent = (EditText) findViewById(R.id.et_content);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        wvBookPlay = (WebView) findViewById(R.id.activity_main_webview1);
        webViewInit(wvBookPlay);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etContent.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(WebSocketActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                WebSocketHandler.getDefault().send(text);

              /*  final InstructionResponse responseEntity = new InstructionResponse();
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
                }).start();*/
            }
        });
        findViewById(R.id.btn_upqiniu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QiniuUpHelper.upload(WebSocketActivity.this, false, new TAKESCREEN.BackUrl() {
                    @Override
                    public String getUrlandName(String backUrlandName) {
                        return null;
                    }
                });

            }
        });

        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskProgarm.progarmTest1(DownLoadService.getDownLoadManager());
            }
        });
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
        StringBuilder textBuilder = new StringBuilder();
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
        });
    }

    @Override
    public void onEvent(Event mEvent) {
        switch (mEvent.getId()) {
            case EVENT_TEST_MSG1:

                Log.d(this.getClass().getName(), "我收到消息啦1");
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
        }
    }

    List<ProgramResource> musicList;
    int index = 0;
    /**
     * 规定开始音乐、暂停音乐、结束音乐的标志
     */
    public static final int PLAT_MUSIC = 1;
    public static final int PAUSE_MUSIC = 2;
    public static final int STOP_MUSIC = 3;

    private void initMediaPlayer() {
        try {
            String path = null;
            ProgramResource programResource = musicList.get(index);
            if (!StringUtils.isEmpty(programResource.getVirtualPath())) {
                path = "file://" + FileHelper.getFileDefaultPath() + "/" + programResource.getVirtualPath();
                Log.e(TAG, "path111" + path);
            }//想要添加判断 是否找到music.map3
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            //为播放器添加播放完成时的监听器
        } catch (Exception e) {
            Toast.makeText(WebSocketActivity.this, "the sd have not music.mp3" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setComp() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                index = index + 1;
                if (index >= musicList.size()) {
                    index = 0;
                }
                initMediaPlayer();
                mediaPlayer.start();
            }
        });
    }
}
