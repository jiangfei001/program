package com.sgs;


public class AppUrl {

    public static String prodIP = "http://xinlianchuangmei.com/";
    public static String testIP = "192.168.43.47:8082";

    public static boolean isTest = true;

    //注册
    public static String serverUrlAddMuTerminal;
    //socket服务
    public static String socketUrl;
    //返回服务处理信息
    public static String callbackUrl;
    //节目列表新增
    public static String addTerminalProgramListUrl;

    static {
        if (isTest) {
            socketUrl = "ws://" + testIP + "";
            serverUrlAddMuTerminal = "http://" + testIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = "http://" + testIP + "/multimedia/api/terminal/callback";
            addTerminalProgramListUrl = "http://" + testIP + "/multimedia/api/terminal/addTerminalProgramList";
        } else {
            socketUrl = "ws://49.235.109.237:9080";
            serverUrlAddMuTerminal = prodIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = prodIP + "/multimedia/api/terminal/callback";
            addTerminalProgramListUrl = prodIP + "/multimedia/api/terminal/addTerminalProgramList";
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