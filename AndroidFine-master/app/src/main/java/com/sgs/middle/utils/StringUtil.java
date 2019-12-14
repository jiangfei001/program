package com.sgs.middle.utils;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.sgs.middle.model.StorageSize;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nick Song
 * @version 1.0
 * @Description StringUtil
 * @date 2018/2/2
 */
public final class StringUtil {

    private static final String HTTP = "http";
    private static final String COLON = ":";

    private StringUtil() {
        // Constructor
    }

    /**
     * 将时间戳转为日期yyyy-MM-dd
     *
     * @param timeStamp
     * @return
     */
    public static String convertStamp2Date(long timeStamp) {
        Date d = new Date(timeStamp);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        return sdr.format(d);
    }

    /**
     * 将时间戳转为日期yyyy/MM/dd
     *
     * @param timeStamp
     * @return
     */
    public static String timeStamp2Date(long timeStamp) {
        Date d = new Date(timeStamp);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd");
        return sdr.format(d);
    }

    /**
     * 将时间戳转为日期时间yyyy/MM/dd HH:mm
     *
     * @param timeStamp
     * @return
     */
    public static String convertStamp2DateTime(long timeStamp) {
        Date d = new Date(timeStamp);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdr.format(d);
    }

    /**
     * 将时间戳转为日期时间yyyy-MM-dd HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeStamp2DateTime(long timeStamp) {
        Date d = new Date(timeStamp);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdr.format(d);
    }

    /**
     * 将日期时间转换为时间戳
     *
     * @param dateString
     * @return 时间戳
     */
    public static long convertDateString2TimeStamp(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            Log.e("DateString", "" + e.getMessage());
        }
        return date.getTime();
    }

    /**
     * 判断字符串是否为空
     *
     * @param string
     * @return true:为空，false:不为空
     */
    public static boolean isEmpty(String string) {
        return !(string != null && !"".equals(string));
    }

    /**
     * 将原始字符串进行MD5
     *
     * @param originalString
     * @return
     */
    public static String md5(String originalString) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = originalString.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            Log.e("E", "StringUtil MD5 exception");
            return "";
        }
    }

    /**
     * 将大小转为带单位的值
     *
     * @param size
     * @return
     */
    public static Pair<String, String> convertSpaceWithUnit(long size) {
        DecimalFormat format = new DecimalFormat("0.00");
        StorageSize storageSize = StorageUtil.convertStorageSize(size);
        String strSize = format.format(storageSize.value);
        String strUnit = storageSize.suffix;
        return new Pair<>(strSize, strUnit);
    }

    /**
     * 将Ram转为带单位的值
     *
     * @param size
     * @return
     */
    public static String convertRamWithUnit(long size) {
        DecimalFormat format = new DecimalFormat("0.00");
        String strSize = format.format(StorageUtil.convertStorageWithMb(size));
        return strSize;
    }


    /**
     * 字符串Unicode解码
     *
     * @param ori
     * @return
     */
    public static String convertUnicode(String ori) {
        char aChar;
        int len = ori.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = ori.charAt(x++);
            if (aChar == '\\') {
                aChar = ori.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = ori.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }

        }
        return outBuffer.toString();
    }

    /**
     * 判断手机号是否符合规范
     *
     * @param phoneNo 输入的手机号
     * @return
     */
    public static boolean isPhoneNumber(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return false;
        }
        if (phoneNo.length() == 11) {
            for (int i = 0; i < 11; i++) {
                if (!PhoneNumberUtils.isISODigit(phoneNo.charAt(i))) {
                    return false;
                }
            }
            Pattern p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
                    "|(14[5,7])" +
                    "|(15[^4,\\D])" +
                    "|(17[3,6-8])" +
                    "|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(phoneNo);
            return m.matches();
        }
        return false;
    }

    public static String getDomainFromUrl(String url) {
        if (url != null && url.startsWith(HTTP)) {
            int index = url.indexOf(COLON);
            return url.substring(index + 3, url.length());
        }

        return "";
    }

    public static String getDomainFromUrlSit(String url) {
        if (url != null && url.startsWith(HTTP)) {
            String[] mUrl = url.split(COLON);
            if (mUrl.length >= 2 && mUrl[1].length() >= 3) {
                return mUrl[1].substring(2, mUrl[1].length());
            }
        }

        return "";
    }

}
