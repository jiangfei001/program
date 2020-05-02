package com.sgs.businessmodule.websocketmodel;

import android.content.Context;

import com.sgs.businessmodule.taskModel.commandModel.orderToDb.InstructionRequestManager;
import com.sgs.businessmodule.taskModel.commandModel.orderToDb.OrderManager;
import com.sgs.middle.dbModel.entity.InstructionRequest;
import com.sgs.AppContext;
import com.jf.websocket.SimpleListener;
import com.jf.websocket.SocketListener;
import com.jf.websocket.WebSocketHandler;
import com.jf.websocket.response.ErrorResponse;

public class WebSocketClientManger {
    private final String TAG = "DataManager";

    private Context context;

    private WebSocketClientManger(Context context) {
        this.context = context;
        WebSocketHandler.getDefault().addListener(socketListener);
    }

    private static WebSocketClientManger instance;

    public static WebSocketClientManger getInstance() {
        if (instance == null) {
            synchronized (OrderManager.class) {
                if (instance == null) {
                    instance = new WebSocketClientManger(AppContext.getInstance());
                }
            }
        }
        return instance;
    }

    private SocketListener socketListener = new SimpleListener() {
        @Override
        public void onConnected() {
           // appendMsgDisplay("onConnected");
        }

        @Override
        public void onConnectFailed(Throwable e) {
            if (e != null) {
               // appendMsgDisplay("onConnectFailed:" + e.toString());
            } else {
               // appendMsgDisplay("onConnectFailed:null");
            }
        }

        @Override
        public void onDisconnect() {
           // appendMsgDisplay("onDisconnect");
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            //appendMsgDisplay(errorResponse.getDescription());
            errorResponse.release();
        }

        @Override
        public <T> void onMessage(String message, T data) {
            if (data instanceof InstructionRequest) {
                InstructionRequestManager.getInstance().saveInstructionRequest((InstructionRequest) data);
                /* InstructionRequest responseEntity = (InstructionRequest) data;*/
               // appendMsgDisplay(data.toString());
            /*    OrderManager.getInstance().saveOrder();*/
              /*  HashMap hashMap = new HashMap();
                ResponseEntity responseEntity=new ResponseEntity();
                responseEntity.setCode(123);

                hashMap.put("ResponseEntity", JSON.toJSON(responseEntity));
                //存库
                HttpClient.post("http://192.168.0.97:8081/multimedia/api/terminal/callback", hashMap, new HttpResponseHandler());*/

            }
        }
    };

}
