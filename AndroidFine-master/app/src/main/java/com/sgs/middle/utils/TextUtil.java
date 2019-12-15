package com.sgs.middle.utils;

import com.sgs.AppContext;

import java.io.IOException;
import java.io.InputStream;

public class TextUtil {
    public static String gettextAssets(String name) {
        try {
            InputStream is = AppContext.getInstance().getAssets().open(name);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "UTF-8");
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
