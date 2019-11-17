package com.testActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.commandModel.orderToDb.InstructionRequestManager;
import com.dbModel.entity.InstructionRequest;
import com.eventControlModel.Event;
import com.eventControlModel.EventEnum;
import com.eventControlModel.EventManager;
import com.httpModel.HttpClient;
import com.httpModel.HttpResponseHandler;
import com.taskModel.ITask;
import com.taskModel.TaskQueue;
import com.taskModel.taskFactory.TaskFactory;
import com.websocketmodel.EventActivity;
import com.websocketmodel.InstructionResponse;
import com.yuzhi.fine.R;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import java.util.Date;
import java.util.Map;

public class MainTestActivity extends EventActivity {

    private EditText etContent;
    private TextView tvMsg;
    private ScrollView scrollView;

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
                InstructionRequest requestEntity = (InstructionRequest) data;
                //显示，以及输出日志
                appendMsgDisplay(data.toString());
                //存库
                InstructionRequestManager.getInstance().saveInstructionRequest((InstructionRequest) data);
                //启动任务
                ITask t = TaskFactory.createTask(requestEntity);
                taskQueue.add(t);
            }
        }
    };

    TaskQueue taskQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);
        initView();
        WebSocketHandler.getDefault().addListener(socketListener);
        taskQueue = new TaskQueue(1);
    }

    private void initView() {
        etContent = (EditText) findViewById(R.id.et_content);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  String text = etContent.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(TestCommandActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                WebSocketHandler.getDefault().send(text);*/

                final InstructionResponse responseEntity = new InstructionResponse();
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
                }).start();
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
            textBuilder.append(tvMsg.getText().toString());
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
        EventManager.register(this);
    }

    @Override
    public void onEvent(Event mEvent) {
        switch (mEvent.getId()) {
            case EVENT_TEST_MSG1:
                Log.d(this.getClass().getName(), "我收到消息啦");
                /* tv_msg.setText("我收到消息啦");*/
                appendMsgDisplay(this.getClass().getName());
                break;
            case EVENT_TEST_MSG2:
                Map event = mEvent.getParams();
                String msg = (String) event.get(EventEnum.EVENT_TEST_MSG2_KEY);
                appendMsgDisplay(msg);
                break;
        }
    }
}
