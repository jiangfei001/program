package com.sgs.businessmodule.websocketmodel;

import com.zhangke.zlog.ZLog;

import com.sgs.AppContext;
import com.sgs.AppUrl;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.middle.utils.Sha256Hash;
import com.jf.websocket.WebSocketHandler;
import com.jf.websocket.WebSocketManager;
import com.jf.websocket.WebSocketSetting;

public class WebSocketHelper {

    public static void initWebSocket(String username) {
        WebSocketSetting setting = new WebSocketSetting();
        //连接地址，必填，例如 wss://echo.websocket.org
        //setting.setConnectUrl("ws://192.168.0.103:8081/multimedia/api/websocket/jf");//必填
        //setting.setConnectUrl("ws://192.168.0.97:8081/multimedia/api/websocket/jf");//必填
        //setting.setConnectUrl("ws://192.168.0.106:8082/multimedia/api/websocket/" + username);//必填
        /* http:// 192.168.0.107:8082/multimedia/api/terminal/addMuTerminal*/

        ZLog.e("initWebSocket", "socketUrl" + AppUrl.socketUrl);
        String systemTime = System.currentTimeMillis() + "";
        String endstr = "/" + DeviceUtil.getTerDeviceID(AppContext.getInstance()) + "/" + systemTime + "/" + Sha256Hash.getToken(DeviceUtil.getTerDeviceID(AppContext.getInstance()), systemTime, DeviceUtil.getSercetKey(AppContext.getInstance()));
        ZLog.e("weiyima:", "endstr" + endstr);
        setting.setConnectUrl(AppUrl.socketUrl + endstr);//必填
        setting.setEnd(endstr);
        setting.setConnectUrls(AppUrl.socketIPList);
        setting.setTeststr(AppUrl.isTeststr);
        //设置连接超时时间
        setting.setConnectTimeout(15 * 1000);

        //设置心跳间隔时间
        setting.setConnectionLostTimeout(60);

        //设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
        setting.setReconnectFrequency(60);

//        //设置Header
//        setting.setHttpHeaders(header);

        //设置消息分发器，接收到数据后先进入该类中处理，处理完再发送到下游
        setting.setResponseProcessDispatcher(new AppResponseDispatcher());
        //接收到数据后是否放入子线程处理，只有设置了 ResponseProcessDispatcher 才有意义
        setting.setProcessDataOnBackground(true);

        //网络状态发生变化后是否重连，
        //需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
        setting.setReconnectWithNetworkChanged(true);

        //通过 init 方法初始化默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.init(setting);
        //启动连接
        manager.start();

        //注意，需要在 AndroidManifest 中配置网络状态获取权限
        //注册网路连接状态变化广播
        WebSocketHandler.registerNetworkChangedReceiver(AppContext.getInstance());
        //初始化监听器
        WebSocketClientManger.getInstance();
    }

}
