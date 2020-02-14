package com.sgs;


public class AppUrl {

    public static String prodIP = "http://xinlianchuangmei.com/";
    public static String testIP = "172.20.10.4:8082";


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

    static {
        if (isTest) {
            socketUrl = "ws://" + testIP + "/multimedia/api/websocket";
            serverUrlAddMuTerminal = "http://" + testIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = "http://" + testIP + "/multimedia/api/terminal/callback";
            addTerminalProgramListUrl = "http://" + testIP + "/multimedia/api/terminal/addTerminalProgramList";
            addDayProgramList = "http://" + testIP + "/multimedia/api/terminal/addDayProgramList";
        } else {
            socketUrl = "ws://49.235.109.237:9090/multimedia_test/api/websocket";
            serverUrlAddMuTerminal = "http://49.235.109.237:9090/multimedia_test/api/terminal/addMuTerminal";
            callbackUrl = "http://49.235.109.237:9090/multimedia_test/api/terminal/callback";
            addTerminalProgramListUrl = "http://49.235.109.237:9090//multimedia_test/api/terminal/addTerminalProgramList";
            addDayProgramList = "http://49.235.109.237:9090/multimedia_test/api/terminal/addDayProgramList";
            /*socketUrl = "ws://49.235.109.237:9080/multimedia/api/websocket";
            serverUrlAddMuTerminal = prodIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = prodIP + "/multimedia/api/terminal/callback";
            addTerminalProgramListUrl = prodIP + "/multimedia/api/terminal/addTerminalProgramList";
            addDayProgramList = prodIP + "/multimedia/api/terminal/addDayProgramList";*/
        }
    }

    public static void initip(String ip, String duankou) {
        isTest = false;
        socketUrl = "ws://" + ip + ":" + duankou + "";
        serverUrlAddMuTerminal = "http://" + ip + ":" + duankou + "/" + "/multimedia/api/terminal/addMuTerminal";
        callbackUrl = "http://" + ip + ":" + duankou + "/" + "/multimedia/api/terminal/callback";
        addTerminalProgramListUrl = "http://" + ip + ":" + duankou + "/" + "/multimedia/api/terminal/addTerminalProgramListUrl";
    }

}