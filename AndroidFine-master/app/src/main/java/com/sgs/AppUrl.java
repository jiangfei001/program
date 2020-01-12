package com.sgs;


public class AppUrl {

    public static String prodIP = "http://xinlianchuangmei.com/";
    public static String testIP = "http://192.168.0.110:8082/";

    public static boolean isTest = false;

    //注册
    public static String serverUrlAddMuTerminal;
    //socket服务
    public static String socketUrl;
    //返回服务处理信息
    public static String callbackUrl;

    static {
        if (isTest) {
            socketUrl = "ws://192.168.0.110:8082";
            serverUrlAddMuTerminal = testIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = testIP + "/multimedia/api/terminal/callback";
        } else {
            socketUrl = "ws://49.235.109.237:9080";
            serverUrlAddMuTerminal = prodIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = prodIP + "/multimedia/api/terminal/callback";
        }
    }

    public static void initip(String ip, String duankou) {
        isTest=false;
        socketUrl = "ws://" + ip + ":" + duankou + "";
        serverUrlAddMuTerminal = "http://" + ip + ":" + duankou + "/" + "/multimedia/api/terminal/addMuTerminal";
        callbackUrl = "http://" + ip + ":" + duankou + "/" + "/multimedia/api/terminal/callback";
    }

}