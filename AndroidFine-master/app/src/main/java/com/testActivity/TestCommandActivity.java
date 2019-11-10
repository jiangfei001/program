package com.testActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dbModel.entity.InstructionRequest;
import com.eventControlModel.Event;
import com.eventControlModel.EventEnum;
import com.eventControlModel.EventManager;
import com.httpModel.HttpClient;
import com.httpModel.HttpResponseHandler;
import com.websocketmodel.EventActivity;
import com.websocketmodel.InstructionResponse;
import com.yuzhi.fine.R;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestCommandActivity extends EventActivity {

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
                InstructionRequest responseEntity = (InstructionRequest) data;
                appendMsgDisplay(data.toString());

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_command);
        initView();
        WebSocketHandler.getDefault().addListener(socketListener);
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

                final HashMap hashMap = new HashMap();
                InstructionResponse responseEntity=new InstructionResponse();
                responseEntity.setId(123);
                responseEntity.setExecuteTime(new Date());
                responseEntity.setResult("123123");
                responseEntity.setStatus(1);
                responseEntity.setReceiveTime(new Date());

                hashMap.put("ResponseEntity", JSON.toJSON(responseEntity));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient.post("http://192.168.0.97:8081/multimedia/api/terminal/callback", hashMap, new HttpResponseHandler());
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
