package com.sgs.next.comcourier.sfservice.fourlevel;

import android.text.TextUtils;


import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串操作工具包
 * Created by Jamie on 2015/11/26.
 */
public class StringUtils {
    public static final char UNDERLINE = '_';

    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 字符串截取
     *
     * @param str
     * @param length
     * @return
     * @throws Exception
     */
    public static String subString(String str, int length) throws Exception {
        if (str == null)
            return null;

        byte[] bytes = str.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = 2; // 要截取的字节数，从第3个字节开始
        for (; i < bytes.length && n < length; i++) {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1) {
                n++; // 在UCS2第二个字节时n加1
            } else {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1) {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0)
                i = i - 1;
                // 该UCS2字符是字母或数字，则保留该字符
            else
                i = i + 1;
        }
        return new String(bytes, 0, i, "Unicode");
    }

    /**
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 计算微博内容的长度 1个汉字 == 两个英文字母所占的长度 标点符号区分英文和中文
     *
     * @param c 所要统计的字符序列
     * @return 返回字符序列计算的长度
     */
    public static long calculateWeiboLength(CharSequence c) {
        if (c == null)
            return 0;

        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 分割字符串
     *
     * @param str       String 原始字符串
     * @param splitsign String 分隔符
     * @return String[] 分割后的字符串数组
     */
    public static String[] split(String str, String splitsign) {
        int index;
        if (str == null || splitsign == null)
            return null;
        ArrayList<String> al = new ArrayList<String>();
        while ((index = str.indexOf(splitsign)) != -1) {
            al.add(str.substring(0, index));
            str = str.substring(index + splitsign.length());
        }
        al.add(str);
        return (String[]) al.toArray(new String[0]);
    }

    /**
     * 替换字符串
     *
     * @param from   String 原始字符串
     * @param to     String 目标字符串
     * @param source String 母字符串
     * @return String 替换后的字符串
     */
    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null)
            return null;
        StringBuffer bf = new StringBuffer("");
        int index;
        while ((index = source.indexOf(from)) != -1) {
            bf.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            //index = source.indexOf(from);
        }
        bf.append(source);
        return bf.toString();
    }

    /**
     * 替换字符串，能能够在HTML页面上直接显示(替换双引号和小于号)
     *
     * @param str String 原始字符串
     * @return String 替换后的字符串
     */
    public static String htmlencode(String str) {
        if (str == null) {
            return null;
        }

        return replace("\"", "&quot;", replace("<", "&lt;", str));
    }

    /**
     * 替换字符串，将被编码的转换成原始码（替换成双引号和小于号）
     *
     * @param str String
     * @return String
     */
    public static String htmldecode(String str) {
        if (str == null) {
            return null;
        }

        return replace("&quot;", "\"", replace("&lt;", "<", str));
    }

    private static final String _BR = "<br/>";

    /**
     * 在页面上直接显示文本内容，替换小于号，空格，回车，TAB
     *
     * @param str String 原始字符串
     * @return String 替换后的字符串
     */
    public static String htmlshow(String str) {
        if (str == null) {
            return null;
        }

        str = replace("<", "&lt;", str);
        str = replace(" ", "&nbsp;", str);
        str = replace("\r\n", _BR, str);
        str = replace("\n", _BR, str);
        str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
        return str;
    }

    /**
     * 返回指定字节长度的字符串
     *
     * @param str    String 字符串
     * @param length int 指定长度
     * @return String 返回的字符串
     */
    public static String toLength(String str, int length) {
        if (str == null) {
            return null;
        }
        if (length <= 0) {
            return "";
        }
        try {
            if (str.getBytes("GBK").length <= length) {
                return str;
            }
        } catch (Exception ex) {
            LogUtils.e(ex.getMessage(), ex);
        }
        StringBuffer buff = new StringBuffer();

        int index = 0;
        char c;
        length -= 3;
        while (length > 0) {
            c = str.charAt(index);
            if (c < 128) {
                length--;
            } else {
                length--;
                length--;
            }
            buff.append(c);
            index++;
        }
        buff.append("...");
        return buff.toString();
    }

    /**
     * 获取url的后缀名
     *
     * @param
     * @return
     */
    public static String getUrlFileName(String urlString) {
        if (urlString == null)
            return null;

        String fileName = urlString.substring(urlString.lastIndexOf("/"));
        fileName = fileName.substring(1, fileName.length());
        if ("".equalsIgnoreCase(fileName)) {
            Calendar c = Calendar.getInstance();
            fileName = Integer.toString(c.get(Calendar.YEAR)) + c.get(Calendar.MONTH) + ""
                    + c.get(Calendar.DAY_OF_MONTH) + ""
                    + c.get(Calendar.MINUTE);

        }
        return fileName;
    }

    public static String replaceSomeString(String str) {
        String dest = "";
        try {
            if (str != null) {
                str = str.replaceAll("\r", "");
                str = str.replaceAll("&gt;", ">");
                str = str.replaceAll("&ldquo;", "“");
                str = str.replaceAll("&rdquo;", "”");
                str = str.replaceAll("&#39;", "'");
                str = str.replaceAll("&nbsp;", "");
                str = str.replaceAll("<br\\s*/>", "\n");
                str = str.replaceAll("&quot;", "\"");
                str = str.replaceAll("&lt;", "<");
                str = str.replaceAll("&lsquo;", "《");
                str = str.replaceAll("&rsquo;", "》");
                str = str.replaceAll("&middot;", "·");
                str = str.replace("&mdash;", "—");
                str = str.replace("&hellip;", "…");
                str = str.replace("&amp;", "×");
                str = str.replaceAll("\\s*", "");
                str = str.trim();
                str = str.replaceAll("<p>", "\n      ");
                str = str.replaceAll("</p>", "");
                str = str.replaceAll("<div.*?>", "\n      ");
                str = str.replaceAll("</div>", "");
                dest = str;
            }
        } catch (Exception e) {
            // TODO: handle exception
            LogUtils.e(e.getMessage(), e);
        }

        return dest;
    }

    //定义script的正则表达式，去除js可以防止注入
    private static final String scriptRegex = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
    private static final String styleRegex = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    //定义HTML标签的正则表达式，去除标签，只提取文字内容
    private static final String htmlRegex = "<[^>]+>";
    //定义空格,回车,换行符,制表符
    private static final String spaceRegex = "\\s*|\t|\r|\n";

    /**
     * 清除文本里面的HTML标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        if (htmlStr == null)
            return null;

        try {
            Pattern p_script = Pattern.compile(scriptRegex,
                    Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            Pattern p_style = Pattern.compile(styleRegex,
                    Pattern.CASE_INSENSITIVE);
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            Pattern p_html = Pattern.compile(htmlRegex,
                    Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
        } catch (Exception e) {
            // TODO: handle exception
            LogUtils.e(e.getMessage(), e);
        }

        return htmlStr; // 返回文本字符串
    }

    /**
     * 去除html代码中含有的标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHtmlTagAndSpace(String htmlStr) {
        if (htmlStr == null)
            return null;

        // 过滤script标签
        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(styleRegex, "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        // 过滤空格等
        htmlStr = htmlStr.replaceAll(spaceRegex, "");

        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 背景：生产问题：小哥输入联系人时，输入的内容是“\b李祥”，BSP接口报错，清单异常
     *
     * @param str
     * @return
     */
    public static String delSpacialCode(String str) {
        if (str == null)
            return null;

        /** 删除转义字符 */
        str = str.replaceAll("\\\\[a-z]", "");
        return str;
    }

    public static String delSpace(String str) {
        if (str != null) {
            str = str.replaceAll("\r", "");
            str = str.replaceAll("\n", "");
            str = str.replace(" ", "");
        }
        return str;
    }

    /**
     * 工号登录时去掉最前面的两个零
     *
     * @param s
     * @return
     */
    public static String replace8to6Emp(String s) {
        if (s != null) {
            if (s.startsWith("00") && s.length() == 8) {
                return s.substring(2);
            }
        }
        return s;
    }

    /**
     * 检查字符串是否存在值，如果为true,
     *
     * @param str 待检验的字符串
     * @return 当 str 不为 null 或 "" 就返回 true
     */
    public static boolean isNotNull(String str) {
        return (str != null && !"".equalsIgnoreCase(str.trim()) && !"null".equalsIgnoreCase(str));
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    public static String trimmy(String str) {
        String dest = "";
        if (str != null) {
            str = str.replaceAll("-", "");
            str = str.replaceAll("\\+", "");
            dest = str;
        }
        return dest;
    }

    public static String replaceBlank(String str) {

        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\r");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equalsIgnoreCase(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    /**
     * 判断是否是6或8位工号
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpNum(String input) {
        if (input == null || "".equals(input) || "null".equalsIgnoreCase(input))
            return false;
        String regex1 = "^\\d{6}$";
        String regex2 = "^\\d{8}$";

        return input.matches(regex1) || input.matches(regex2);
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是否是手机号码格式
     *
     * @param telphone
     * @return
     */
    public static boolean isTelphoneFormat(String telphone) {
        if (telphone == null) {
            return false;
        }
        String regex = "[1][3456789]\\d{9}";
        return telphone.matches(regex);
    }

    /**
     * 判断是否是密码格式
     *
     * @param password
     * @return
     */
    public static boolean isPasswordFormat(String password) {
        if (password == null) {
            return false;
        }
        String regex = "^^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return password.matches(regex);
    }

    /**
     * 判断是否是车牌格式
     *
     * @param plateNumber
     * @return
     */
    public static boolean isPlateNumberFormat(String plateNumber) {
        if (plateNumber == null) {
            return false;
        }
        String regex = "^[\u4e00-\u9fa5|WJ]{1}[A-Z]{1}[A-Z0-9]{5}$";
        return plateNumber.matches(regex);
    }


    /**
     * 解析money显示格式
     *
     * @param bigDecimal
     * @return
     */
    public static String parseMoneyFormat(BigDecimal bigDecimal) {
        String result = "0.00";
        if (null != bigDecimal) {
            result = new java.text.DecimalFormat("0.00").format(bigDecimal);
        }
        return result;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 判断是不是合法手机 handset 手机号码
     */
    public static boolean isHandset(String handset) {
        try {
            if (!("1").equals(handset.substring(0, 1))) {
                return false;
            }
            if (handset.length() != 11) {
                return false;
            }
            String check = "^[0123456789]+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(handset);
            boolean isMatched = matcher.matches();
            if (isMatched) {
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e) {
            LogUtils.e(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 判断输入的字符串是否为纯汉字
     *
     * @param str 传入的字符窜
     * @return 如果是纯汉字返回true, 否则返回false
     */
    public static boolean isChinese(String str) {
        if (isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        return pattern.matcher(str).matches();
    }

    private static final Pattern numberPattern = Pattern.compile("\\d+");

    public static boolean isNumber(String text) {
        return text != null && numberPattern.matcher(text).matches();
    }


    /**
     * 判断输入的字符串是否为纯英文
     *
     * @param str 传入的字符窜
     * @return 如果是纯英文返回true, 否则返回false
     */
    public static boolean isEnglish(String str) {
        if (isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        if (isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为浮点数，包括double和float
     *
     * @param str 传入的字符串
     * @return 是浮点数返回true, 否则返回false
     */
    public static boolean isDouble(String str) {
        if (isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 是否为空白,包括null和""
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断是否是指定长度的字符串
     *
     * @param text   字符串
     * @param lenght 自定的长度
     * @return
     */
    public static boolean isLenghtStrLentht(String text, int lenght) {
        if (text == null)
            return false;

        if (text.length() <= lenght)
            return true;
        else
            return false;
    }

    /**
     * 是否是短信的长度
     *
     * @param text
     * @return
     */
    public static boolean isSMSStrLentht(String text) {
        if (text == null)
            return false;

        if (text.length() <= 70)
            return true;
        else
            return false;
    }

    // 判断是否为url
    public static boolean checkEmail(String email) {
        if (isEmpty(email))
            return false;

        Pattern pattern = Pattern
                .compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    // 判断微博分享是否为是否为120个
    public static boolean isShareStrLentht(String text, int lenght) {
        if (text == null)
            return false;

        if (text.length() <= 120)
            return true;
        else
            return false;
    }

    public static String getFileNameFromUrl(String url) {
        if (url == null)
            return null;

        // 名字不能只用这个
        // 通过 ‘？’ 和 ‘/’ 判断文件名
        String extName;
        String filename;
        int index = url.lastIndexOf('?');
        if (index > 1) {
            extName = url.substring(url.lastIndexOf('.') + 1, index);
        } else {
            extName = url.substring(url.lastIndexOf('.') + 1);
        }
        filename = hashKeyForDisk(url) + "." + extName;
        return filename;
        /*
         * int index = url.lastIndexOf('?'); String filename; if (index > 1) {
         * filename = url.substring(url.lastIndexOf('/') + 1, index); } else {
         * filename = url.substring(url.lastIndexOf('/') + 1); }
         *
         * if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
         * filename = UUID.randomUUID() + ".apk";// 默认取一个文件名 } return filename;
         */
    }

    /**
     * 一个散列方法,改变一个字符串(如URL)到一个散列适合使用作为一个磁盘文件名。
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
            LogUtils.e(e.getMessage(), e);
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null)
            return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static byte[] getGBKBytes(String str) {
        if (str == null) return null;
        byte[] bytes;
        try {
            bytes = str.getBytes("GBK");
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            try {
                bytes = str.getBytes("gbk");
            } catch (Exception e2) {
                bytes = str.getBytes();
                LogUtils.e(e2.getMessage(), e2);
            }
        }
        return bytes;
    }


    /**
     * @param @param  param
     * @param @return
     * @return String
     * @throws
     * @Title: underlineToCamel
     * @Description: 下划线转驼峰命名
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 生成唯一的requestId
     *
     * @param str        预留字符串
     * @param moduleName 各个模块名
     * @return
     */
    public static String generateUniqueRequestId(String str, String moduleName) {
        UUID uuid = UUID.randomUUID();
        return str + moduleName + uuid.toString();
    }

    /**
     * 生成唯一的requestId
     *
     * @param moduleName 各个模块名
     * @return
     */
    public static String generateUniqueRequestId(String moduleName) {
        UUID uuid = UUID.randomUUID();
        return moduleName + uuid.toString();
    }

    /**
     * 根据手机号码显示名字
     *
     * @param mobile
     * @param username
     * @return
     */
    public static String showUserName(String mobile, String username) {
        if (!isEmpty(mobile)) {
            if (isMobileNO(mobile)) {
                try {
                    return subString(username, 3) + "*";
                } catch (Exception e) {
                    LogUtils.e(e.getMessage(), e);
                    return username;
                }
            } else {
                return username;
            }
        }
        return username;
    }

    public static String getStr(String str) {
        return !StringUtils.isEmpty(str) ? str : "";
    }

    /**
     * 判断是否是手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        if (mobiles == null)
            return false;

        Pattern p = Pattern.compile("^((13[0-9])|(147)|(15[^4,\\D])|(17[0-8])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 出于安全信息考虑，输出null如果是空，否则输出空
     *
     * @param obj
     * @return
     */
    public static String isNullStr(Object obj) {
        try {
            if (obj == null) {
                return "null";
            } else {
                if (obj instanceof Collection) {
                    if (CollectionUtils.isEmpty((Collection) obj)) {
                        return "null";
                    } else {
                        return "{size=" + ((Collection) obj).size() + "}";
                    }
                } else if (obj instanceof Map) {
                    if (CollectionUtils.isEmpty((Map) obj)) {
                        return "null";
                    } else {
                        return "{size=" + ((Map) obj).size() + "}";
                    }
                }
                return obj + "";
            }
        } catch (Exception ex) {
            LogUtils.e(ex.getMessage(), ex);
            return "";
        }
    }

    /**
     * 判断是否是固定电话
     *
     * @param tel
     * @return
     */
    public static boolean isLandlineTel(String tel) {
        if (tel == null)
            return false;
        Pattern p = Pattern.compile("^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$");
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    /**
     * 判断是否含有数字，若输入为空，视为不含有
     *
     * @param input
     * @return
     */
    public static boolean containNumber(String input) {
        if (!isEmpty(input)) {
            Pattern p = Pattern.compile(".*\\d.*");
            Matcher m = p.matcher(input);
            return m.matches();
        }
        return false;
    }

    /**
     * 判断是否包含电话号码 包括 138-0807-4428 和 138 0807 4428 这种形式
     *
     * @param input
     * @return
     */
    public static boolean containTel(String input) {
        if (!isEmpty(input)) {
            Pattern p = Pattern.compile(".*[1]\\d{2}[-|\\s]?\\d{4}[-|\\s]?\\d{4}.*");
            Matcher m = p.matcher(input);
            return m.matches();
        }
        return false;
    }


    /**
     * 运单号显示处理：每3个字符加空格
     *
     * @param waybill
     * @return
     */
    public static String addSpaceOfWaybill(String waybill) {
        if (waybill == null || waybill.isEmpty()) {
            return "";
        }

        StringBuilder vBuilder = new StringBuilder();
        for (int i = 0; i < waybill.length(); i++) {
            vBuilder.append(waybill.charAt(i));

            if (i % 3 == 2) {
                vBuilder.append(" ");
            }
        }

        return vBuilder.toString();
    }


    /**
     * 日志输出null如果是空
     *
     * @param obj
     * @return
     */
    public static String ifNullStr(Object obj) {
        if (obj == null) {
            return "null";
        } else {
            if (obj instanceof String) {
                return (String) obj;
            } else if (obj instanceof Collection) {
                StringBuilder builder = new StringBuilder();
                builder.append("size=" + (CollectionUtils.isEmpty((Collection) obj) ? 0 : ((Collection) obj).size()));
                builder.append("[");
                for (Object temp : (Collection) obj) {
                    builder.append(temp.toString() + ",\r\n");
                }
                builder.append("]");
                return builder.toString();
            } else {
                return obj.toString();
            }
        }
    }

    public static String join(List<String> lst, String element) {
        if (lst == null) {
            return "";
        }
        Iterator iterator = lst.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(element);
            }
        }
        return sb.toString();
    }

    /**
     * 数组去除重复数据
     */
    public static List<String> getFilterList(String[] mArray) {
        List<String> mList = new ArrayList<>();
        if (null != mArray && mArray.length > 0) {
            for (int i = 0; i < mArray.length; i++) {
                if (!mList.contains(mArray[i])) {
                    mList.add(mArray[i]);
                }
            }
        }
        return mList;
    }

    private static final Pattern whiteSpacePattern = Pattern.compile("\\s+");

    public static String removeAllWhiteSpace(String text) {
        return text == null ? null : whiteSpacePattern.matcher(text).replaceAll("");
    }


    public static String stringFilter(String str) {
        try {
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& ;*（）——+|{}【】‘；：”“’。，、？_\"\"|-]";//要过滤掉的字符
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } catch (Exception e) {
            // ComuiLogUtils.e(e);
        }
        return "";
    }


    /**
     * 字符串转bigDecimal
     *
     * @param bigDecimal
     * @return 转换异常返回
     */
    public static BigDecimal toBigDecimal(String bigDecimal) {
        try {
            if (!StringUtils.isEmpty(bigDecimal)) {
                return new BigDecimal(bigDecimal);
            }
        } catch (Exception e) {
            //ComCourierLogUtils.e(e);
        }
        return new BigDecimal(0);
    }

    public static final String DEFAULT_VALUE_LOGIN_SESSION_ORIGIN_CODE = "-1";

    /**
     * 根据网点获取城市名称
     *
     * @param originCode 网点代码
     * @return
     */
    public static String getCityCodeString(String originCode) {
        if (DEFAULT_VALUE_LOGIN_SESSION_ORIGIN_CODE.equals(originCode)) {
            return DEFAULT_VALUE_LOGIN_SESSION_ORIGIN_CODE;
        }

        if (TextUtils.isEmpty(originCode) || originCode.length() < 3)
            return DEFAULT_VALUE_LOGIN_SESSION_ORIGIN_CODE;

        String left3Chars = originCode.substring(0, 3);
        if (left3Chars.matches("[a-zA-Z]{3}")) {
            return left3Chars;
        }

        Pattern pattern = Pattern.compile("\\d{3,4}");
        Matcher matcher = pattern.matcher(originCode);
        if (matcher.find()) {
            return matcher.group();
        }
        return DEFAULT_VALUE_LOGIN_SESSION_ORIGIN_CODE;
    }


    /****************************************************/


    public static String strArray2Str(String[] list) {
        if (null == list || list.length == 0)
            return "";

        StringBuilder vBuild = new StringBuilder();
        for (String str : list) {
            vBuild.append(str);
            vBuild.append(",");
        }

        if (vBuild.length() > 0) {
            vBuild.deleteCharAt(vBuild.length() - 1);
        }

        return vBuild.toString();
    }


    public static String formatPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        return decimalFormat.format(price);
    }


    /**
     * 删除换行符
     */
    public static String delLineBreak(String str) {
        try {
            if (str != null) {
                str = str.replaceAll("\r", "");
                str = str.replaceAll("\n", "");
                str = str.replace("\\", "");
            }
        } catch (Exception e) {
            //ComuiLogUtils.e(e);
        }
        return str;
    }


}
