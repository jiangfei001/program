package com.uiModel.loginUtil;

import android.text.TextUtils;

import com.sgs.middle.utils.SharedPreferences;

public class LoginUtil {
    public static boolean isjihuo() {
        String terminalIdentity = SharedPreferences.getInstance().getString("terminalIdentity", "");
        String secretKey = SharedPreferences.getInstance().getString("secretKey", "");
        if (!TextUtils.isEmpty(terminalIdentity) && !TextUtils.isEmpty(secretKey)) {
            return true;
        } else {
            return false;
        }
    }

    public static void putTerminalIdentity(String terminalIdentity) {
        SharedPreferences.getInstance().putString("terminalIdentity", "");
    }

    public static void putIsZhuche(Boolean iszhuce) {
        SharedPreferences.getInstance().putBoolean("iszhuce", iszhuce);
    }

    public static boolean getIsZhuche() {
        return SharedPreferences.getInstance().getBoolean("iszhuce", false);
    }

    public static void putSecretKey(String secretKey) {
        SharedPreferences.getInstance().putString("secretKey", "");
    }

    public static void putTerminalIdenAndSecretKey(String terminalIdentity, String secretKey) {
        SharedPreferences.getInstance().putString("terminalIdentity", "");
        SharedPreferences.getInstance().putString("secretKey", "");
    }

    public static String getTerminalIdentity() {
        String terminalIdentity = SharedPreferences.getInstance().getString("terminalIdentity", "");
        return terminalIdentity;
    }

    public static String getSecretKey() {
        String secretKey = SharedPreferences.getInstance().getString("secretKey", "");
        return secretKey;
    }
}
