package com.sgs.businessmodule.httpModel;

import com.sgs.AppContext;
import com.sgs.middle.utils.DeviceUtil;
import com.sgs.middle.utils.Sha256Hash;
import com.uiModel.loginUtil.LoginUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Request;

public class ComRequestManager {
    public static void addRequestHeader(Request.Builder builder) {
        HashMap<String, String> headerMap = ComRequestManager.buildHeader();
        Iterator<Map.Entry<String, String>> iterator = headerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            builder.addHeader(key, entry.getValue());
        }
    }

    public static HashMap<String, String> buildHeader() {
        final HashMap<String, String> map = new HashMap<>();
        String timeSta = System.currentTimeMillis() + "";
        map.put("token", Sha256Hash.getToken(LoginUtil.getTerminalIdentity(), timeSta, LoginUtil.getSecretKey()));
        map.put("terminalId", LoginUtil.getTerminalIdentity());
        map.put("timeStamp", timeSta);
        map.put("Connection", "close");
        map.put("MultipleDevicesAuth", "true");
        map.put("Content-Type", "application/json;charset=UTF-8");
        return map;
    }
}
