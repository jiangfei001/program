package com.sgs;


import android.util.Log;

public class AppUrl {


    public static boolean isTest = false;

    //注册
    public static String serverUrlAddMuTerminal;
    //socket服务
    public static String socketUrl;
    //返回服务处理信息
    public static String callbackUrl;
    //节目列表新增
    public static String addTerminalProgramListUrl;
    //节目列表新增
    public static String addDayProgramList;

    public static String socketIP = "49.235.109.237:9090";
    public static String jiekouIP = "49.235.109.237:9090";

    /*public static String socketIP = "49.235.109.237:9080";
    public static String jiekouIP = "xinlianchuangmei.com";*/


    //  public static String jiekouIP = "http://xinlianchuangmei.com/";

    static {
        socketUrl = "ws://" + socketIP + "/multimedia_test/api/websocket";
        serverUrlAddMuTerminal = "http://" + jiekouIP + "/multimedia_test/api/terminal/addMuTerminal";
        callbackUrl = "http://" + jiekouIP + "/multimedia_test/api/terminal/callback";
        addTerminalProgramListUrl = "http://" + jiekouIP + "/multimedia_test/api/terminal/addTerminalProgramList";
        addDayProgramList = "http://" + jiekouIP + "/multimedia_test/api/terminal/addDayProgramList";
            /*socketUrl = "ws://49.235.109.237:9080/multimedia/api/websocket";
            serverUrlAddMuTerminal = prodIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = prodIP + "/multimedia/api/terminal/callback";
            addTerminalProgramListUrl = prodIP + "/multimedia/api/terminal/addTerminalProgramList";
            addDayProgramList = prodIP + "/multimedia/api/terminal/addDayProgramList";*/
    }


    public static void initip(String ipstr, String jiekouIPstr, String isTeststr) {
        isTest = false;
        socketIP = ipstr;
        jiekouIP = jiekouIPstr;
        socketUrl = "ws://" + socketIP + "/multimedia" + isTeststr.trim() + "/api/websocket";
        serverUrlAddMuTerminal = "http://" + jiekouIP + "/multimedia" + isTeststr.trim() + "/api/terminal/addMuTerminal";
        callbackUrl = "http://" + jiekouIP + "/multimedia" + isTeststr.trim() + "/api/terminal/callback";
        addTerminalProgramListUrl = "http://" + jiekouIP + "/multimedia" + isTeststr.trim() + "/api/terminal/addTerminalProgramList";
        addDayProgramList = "http://" + jiekouIP + "/multimedia" + isTeststr.trim() + "/api/terminal/addDayProgramList";
        Log.e("socketUrl", "socketUrl:" + socketUrl + "callbackUrl:" + callbackUrl);
    }

}