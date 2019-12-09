package com.websocketmodel;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dbModel.entity.InstructionRequest;
import com.downloadModel.DownLoadService;
import com.downloadModel.dbcontrol.FileHelper;
import com.eventControlModel.Event;
import com.eventControlModel.EventEnum;
import com.eventControlModel.EventManager;
import com.programModel.ProgramScheduledManager;
import com.qiniuModel.QiniuUpHelper;
import com.taskModel.TVTask;
import com.programModel.TaskProgarm;
import com.taskModel.TaskQueue;
import com.taskModel.taskFactory.TaskFactory;
import com.yuzhi.fine.R;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import java.util.Map;

public class WebSocketActivity extends EventActivity {

    private String TAG = "WebSocketActivity";
    private EditText etContent;
    private TextView tvMsg;
    private ScrollView scrollView;
    WebView webView;
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

    public void initSchedule() {
        ProgramScheduledManager programScheduledManager = ProgramScheduledManager.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);
        initView();
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
        webView = (WebView) findViewById(R.id.activity_main_webview1);
        EventManager.register(this);

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
                QiniuUpHelper.upload(WebSocketActivity.this, false);

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
                Log.d(this.getClass().getName(), "我收到消息啦");
                webView.getSettings().setJavaScriptEnabled(true);
                String path = mEvent.getPath();
                Log.e(TAG, "file://" + FileHelper.getFileDefaultPath() + "/" + path);
                webView.loadUrl("file://" + FileHelper.getFileDefaultPath() + "/" + path);
                break;
            case EVENT_TEST_MSG2:
                Map event = mEvent.getParams();
                String msg = (String) event.get(EventEnum.EVENT_TEST_MSG2_KEY);
                appendMsgDisplay(msg);
                break;
        }
    }
}
