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
    //节目列表新增
    public static String addRepPalyProgramList;
    //节目列表新增
    public static String addRepHotareaClickList;

    public static String socketIP = "49.235.109.237:9080";
    public static String jiekouIP = "xinlianchuangmei.com";

    public static String socketIPTest = "49.235.109.237:9090";
    public static String jiekouIPTest = "49.235.109.237:9090";

    //  public static String jiekouIP = "http://xinlianchuangmei.com/";

    public static String jiekouUrl = "";

    static {
        socketUrl = "ws://" + socketIP + "/multimedia_test/api/websocket";
        jiekouUrl = "http://" + jiekouIP;
        serverUrlAddMuTerminal = jiekouUrl + "/multimedia_test/api/terminal/addMuTerminal";
        callbackUrl = jiekouUrl + "/multimedia_test/api/terminal/callback";
        addTerminalProgramListUrl = jiekouUrl + "/multimedia_test/api/terminal/addTerminalProgramList";
        addDayProgramList = jiekouUrl + "/multimedia_test/api/terminal/addDayProgramList";
        addRepPalyProgramList = jiekouUrl + "/multimedia_test/api/terminal/addRepPalyProgramList";
        addRepHotareaClickList = jiekouUrl + "/multimedia_test/api/terminal/addRepHotareaClickList";
            /*socketUrl = "ws://49.235.109.237:9080/multimedia/api/websocket";
            serverUrlAddMuTerminal = prodIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = prodIP + "/multimedia/api/terminal/callback";
            addTerminalProgramListUrl = prodIP + "/multimedia/api/terminal/addTerminalProgramList";
            addDayProgramList = prodIP + "/multimedia/api/terminal/addDayProgramList";*/
    }


    public static void initip(boolean isTeststr) {
        if (isTeststr) {
            socketIP = socketIPTest;
            jiekouIP = jiekouIPTest;
        } else {
            socketIP = "49.235.109.237:9080";
            jiekouIP = "xinlianchuangmei.com";
        }

        socketUrl = "ws://" + socketIP + "/multimedia" + (isTeststr ? "_test" : "") + "/api/websocket";
        jiekouUrl = "http://" + jiekouIP;
        serverUrlAddMuTerminal = jiekouUrl + "/multimedia" + (isTeststr ? "_test" : "") + "/api/terminal/addMuTerminal";
        callbackUrl = jiekouUrl + "/multimedia" + (isTeststr ? "_test" : "") + "/api/terminal/callback";
        addTerminalProgramListUrl = jiekouUrl + "/multimedia" + (isTeststr ? "_test" : "") + "/api/terminal/addTerminalProgramList";
        addDayProgramList = jiekouUrl + "/multimedia" + (isTeststr ? "_test" : "") + "/api/terminal/addDayProgramList";
        addRepPalyProgramList = jiekouUrl + "/multimedia" + (isTeststr ? "_test" : "") + "/api/terminal/addRepPalyProgramList";
        addRepHotareaClickList = jiekouUrl + "/multimedia" + (isTeststr ? "_test" : "") + "/api/terminal/addRepHotareaClickList";

        Log.e("socketUrl", "socketIP:" + socketIP + "jiekouIP:" + jiekouIP + "socketUrl" + socketUrl);
        Log.e("socketUrl", "callbackUrl:" + callbackUrl);
        Log.e("socketUrl", "addTerminalProgramListUrl:" + addTerminalProgramListUrl);
        Log.e("socketUrl", "addDayProgramList:" + addDayProgramList);
        Log.e("socketUrl", "addRepPalyProgramList:" + addRepPalyProgramList);
    }

}