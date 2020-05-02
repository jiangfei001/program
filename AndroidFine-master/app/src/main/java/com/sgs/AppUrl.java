package com.sgs;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sgs.middle.utils.StringUtil;
import com.umeng.commonsdk.debug.E;

import java.util.ArrayList;
import java.util.LinkedList;

public class AppUrl {


    public static String shebeiHao = "cea18188037d6f3504902bf2cd6e01b3";

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
    //激活
    public static String activation;

    public static String socketIP = "49.235.109.237:9080";
    public static String jiekouIP = "xinlianchuangmei.com";

    public static String socketIPTest = "49.235.109.237:9090";
    public static String jiekouIPTest = "49.235.109.237:9090";


    //  public static String jiekouIP = "http://xinlianchuangmei.com/";

    public static String jiekouUrl = "";

    public static ArrayList<String> socketIPList = new ArrayList();

    //注册
    public static String getServerList = "";
    private static int index = 0;

    static {
        socketUrl = "ws://" + socketIP + "/multimedia_test/api/websocket";
        jiekouUrl = "http://" + jiekouIP;
        serverUrlAddMuTerminal = jiekouUrl + "/multimedia_test/api/terminal/addMuTerminal";
        callbackUrl = jiekouUrl + "/multimedia_test/api/terminal/callback";
        addTerminalProgramListUrl = jiekouUrl + "/multimedia_test/api/terminal/addTerminalProgramList";
        addDayProgramList = jiekouUrl + "/multimedia_test/api/terminal/addDayProgramList";
        addRepPalyProgramList = jiekouUrl + "/multimedia_test/api/terminal/addRepPalyProgramList";
        addRepHotareaClickList = jiekouUrl + "/multimedia_test/api/terminal/addRepHotareaClickList";
        activation = jiekouUrl + "/multimedia_test/api/terminal/activation";
        getServerList = jiekouUrl + "/multimedia_test/api/terminal/getServerList";
            /*socketUrl = "ws://49.235.109.237:9080/multimedia/api/websocket";
            serverUrlAddMuTerminal = prodIP + "/multimedia/api/terminal/addMuTerminal";
            callbackUrl = prodIP + "/multimedia/api/terminal/callback";
            addTerminalProgramListUrl = prodIP + "/multimedia/api/terminal/addTerminalProgramList";
            addDayProgramList = prodIP + "/multimedia/api/terminal/addDayProgramList";*/
    }

    public static boolean isTeststr;

    public static void initip(boolean isTeststr1) {
        isTeststr = isTeststr1;
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
        activation = jiekouUrl + "/multimedia" + (isTeststr ? "_test" : "") + "/api/terminal/activation";
        getServerList = jiekouUrl + "/multimedia" + (isTeststr ? "_test" : "") + "/api/terminal/getServerList";

        Log.e("socketUrl", "socketIP:" + socketIP + "jiekouIP:" + jiekouIP + "socketUrl" + socketUrl);
        Log.e("socketUrl", "callbackUrl:" + callbackUrl);
        Log.e("socketUrl", "addTerminalProgramListUrl:" + addTerminalProgramListUrl);
        Log.e("socketUrl", "addDayProgramList:" + addDayProgramList);
        Log.e("socketUrl", "addRepPalyProgramList:" + addRepPalyProgramList);
        Log.e("socketUrl", "activation:" + activation);
        Log.e("socketUrl", "getServerList:" + getServerList);
    }

    public static void setSerList(String jsonstr) {
        Log.e("jsonstr", "jsonstr:" + jsonstr);
        try {
            if (!StringUtil.isEmpty(jsonstr)) {
                socketIPList = (ArrayList) JSON.parseArray(jsonstr, String.class);
                Log.e("jsonstr", "Linksize" + socketIPList.size());
                socketIPList.add("49.235.109.237:9080");
                if (socketIPList.size() > 0) {
                    socketIP = socketIPList.get(index);
                    Log.e("jsonstr", "socketIP:" + socketIP);
                    socketUrl = "ws://" + socketIP + "/multimedia" + (isTeststr ? "_test" : "") + "/api/websocket";
                    Log.e("jsonstr", "socketUrl:" + socketIP + "|" + socketUrl);
                }
            }
        } catch (Exception e) {
            Log.e("e", "e:" + e.getMessage());
        }
    }


    public static void setNextIp() {
        if (index < socketIPList.size() - 1) {
            index++;
        } else {
            index = 0;
        }
        socketIP = socketIPList.get(index);
        socketUrl = "ws://" + socketIP + "/multimedia" + (isTeststr ? "_test" : "") + "/api/websocket";
        Log.e("jsonstr", "socketUrl:" + socketIP + "|" + socketUrl);
    }
}